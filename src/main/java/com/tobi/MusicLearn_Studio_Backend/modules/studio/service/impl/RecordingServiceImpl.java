package com.tobi.MusicLearn_Studio_Backend.modules.studio.service.impl;

import com.tobi.MusicLearn_Studio_Backend.common.exceptions.BadRequestException;
import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import com.tobi.MusicLearn_Studio_Backend.common.service.R2StorageService;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.request.CreateRecordingRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.response.RecordingResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.entity.Recording;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.repository.RecordingRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.service.RecordingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordingServiceImpl implements RecordingService {

    private final RecordingRepository recordingRepository;
    private final R2StorageService r2StorageService;

    @Override
    @Transactional
    public RecordingResponse uploadRecording(MultipartFile file, CreateRecordingRequest request, String userId)
            throws IOException {
        log.info("Uploading recording for project: {} by user: {}", request.getProjectId(), userId);

        // Validate audio file
        if (!r2StorageService.validateAudioFile(file)) {
            throw new BadRequestException("Invalid audio file. Only audio files up to 50MB are allowed.");
        }

        // Upload file to R2 storage
        String fileUrl = r2StorageService.uploadFile(file, "studio/recordings");

        // Create recording entity
        Recording recording = Recording.builder()
                .projectId(request.getProjectId())
                .userId(userId)
                .fileUrl(fileUrl)
                .fileName(request.getFileName())
                .duration(request.getDuration())
                .sampleRate(request.getSampleRate())
                .format(request.getFormat())
                .fileSize(file.getSize())
                .build();

        Recording saved = recordingRepository.save(recording);
        log.info("Recording uploaded successfully: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    public List<RecordingResponse> getRecordingsByProject(String projectId, String userId) {
        log.info("Fetching recordings for project: {}", projectId);

        List<Recording> recordings = recordingRepository.findByProjectIdAndIsDeletedFalse(projectId);

        return recordings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecordingResponse> getMyRecordings(String userId) {
        log.info("Fetching recordings for user: {}", userId);

        List<Recording> recordings = recordingRepository.findByUserIdAndIsDeletedFalse(userId);

        return recordings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RecordingResponse getRecordingById(String recordingId, String userId) {
        log.info("Fetching recording: {} by user: {}", recordingId, userId);

        Recording recording = recordingRepository.findById(recordingId)
                .orElseThrow(() -> new ResourceNotFoundException("Recording not found"));

        // Verify ownership
        if (!recording.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied to this recording");
        }

        return mapToResponse(recording);
    }

    @Override
    @Transactional
    public void deleteRecording(String recordingId, String userId) {
        log.info("Deleting recording: {} by user: {}", recordingId, userId);

        Recording recording = recordingRepository.findById(recordingId)
                .orElseThrow(() -> new ResourceNotFoundException("Recording not found"));

        // Verify ownership
        if (!recording.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied to delete this recording");
        }

        // Delete file from R2 storage
        if (recording.getFileUrl() != null) {
            r2StorageService.deleteFile(recording.getFileUrl());
        }

        // Soft delete
        recording.setIsDeleted(true);
        recordingRepository.save(recording);

        log.info("Recording deleted successfully: {}", recordingId);
    }

    private RecordingResponse mapToResponse(Recording recording) {
        return RecordingResponse.builder()
                .id(recording.getId())
                .projectId(recording.getProjectId())
                .userId(recording.getUserId())
                .fileUrl(recording.getFileUrl())
                .fileName(recording.getFileName())
                .duration(recording.getDuration())
                .sampleRate(recording.getSampleRate())
                .format(recording.getFormat())
                .fileSize(recording.getFileSize())
                .createdAt(recording.getCreatedAt())
                .build();
    }
}
