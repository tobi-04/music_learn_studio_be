package com.tobi.MusicLearn_Studio_Backend.common.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class R2StorageService {

    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String publicUrl;
    private final boolean enabled;

    public R2StorageService(
            @Value("${r2.account-id:}") String accountId,
            @Value("${r2.access-key:}") String accessKey,
            @Value("${r2.secret-key:}") String secretKey,
            @Value("${r2.bucket-name:musiclearn-storage}") String bucketName,
            @Value("${r2.public-url:https://example.com}") String publicUrl) {

        this.bucketName = bucketName;
        this.publicUrl = publicUrl;

        // Check if R2 is properly configured
        if (accountId == null || accountId.isEmpty() ||
                accessKey == null || accessKey.isEmpty() ||
                secretKey == null || secretKey.isEmpty()) {

            this.enabled = false;
            this.s3Client = null;
            log.warn("R2 Storage Service is DISABLED - Missing configuration. File uploads will not work.");
            log.warn("Please set R2_ACCOUNT_ID, R2_ACCESS_KEY_ID, and R2_SECRET_ACCESS_KEY in .env file");
            return;
        }

        this.enabled = true;
        String endpoint = String.format("https://%s.r2.cloudflarestorage.com", accountId);

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        this.s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endpoint, "auto"))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        log.info("R2 Storage Service ENABLED with bucket: {}", bucketName);
    }

    /**
     * Upload a file to R2 storage
     *
     * @param file   Multipart file to upload
     * @param folder Folder path in bucket (e.g., "audio", "sheets")
     * @return Public URL of uploaded file
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (!enabled) {
            throw new IOException("R2 Storage is not configured. Please set R2 environment variables.");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Generate unique file key
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        String fileKey = String.format("%s/%s%s", folder, UUID.randomUUID(), fileExtension);

        // Prepare metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // Upload to R2
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    fileKey,
                    inputStream,
                    metadata).withCannedAcl(CannedAccessControlList.PublicRead);

            s3Client.putObject(putObjectRequest);

            log.info("File uploaded successfully: {}", fileKey);
        } catch (Exception e) {
            log.error("Error uploading file to R2: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file to R2 storage", e);
        }

        // Return public URL
        return String.format("%s/%s", publicUrl, fileKey);
    }

    /**
     * Delete a file from R2 storage
     *
     * @param fileUrl Full URL of the file to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteFile(String fileUrl) {
        if (!enabled) {
            log.warn("R2 Storage is not configured. Cannot delete file.");
            return false;
        }

        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }
        try {
            // Extract file key from URL
            String fileKey = fileUrl.replace(publicUrl + "/", "");

            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, fileKey);
            s3Client.deleteObject(deleteObjectRequest);

            log.info("File deleted successfully: {}", fileKey);
            return true;
        } catch (Exception e) {
            log.error("Error deleting file from R2: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Validate file type and size
     *
     * @param file         File to validate
     * @param allowedTypes List of allowed MIME types
     * @param maxSizeBytes Maximum file size in bytes
     * @return true if valid, false otherwise
     */
    public boolean validateFile(MultipartFile file, List<String> allowedTypes, long maxSizeBytes) {
        if (file == null || file.isEmpty()) {
            log.warn("File validation failed: File is null or empty");
            return false;
        }

        // Check file size
        if (file.getSize() > maxSizeBytes) {
            log.warn("File validation failed: File size {} exceeds maximum {}",
                    file.getSize(), maxSizeBytes);
            return false;
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            log.warn("File validation failed: Content type {} not in allowed types {}",
                    contentType, allowedTypes);
            return false;
        }

        return true;
    }

    /**
     * Validate audio file (MP3 only, max 50MB)
     */
    public boolean validateAudioFile(MultipartFile file) {
        return validateFile(file, Arrays.asList("audio/mpeg", "audio/mp3"), 50 * 1024 * 1024);
    }

    /**
     * Validate sheet file (PDF, JPG, PNG, max 10MB)
     */
    public boolean validateSheetFile(MultipartFile file) {
        return validateFile(
                file,
                Arrays.asList("application/pdf", "image/jpeg", "image/png", "image/jpg"),
                10 * 1024 * 1024);
    }
}
