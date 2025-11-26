package com.tobi.MusicLearn_Studio_Backend.modules.music.service.impl;

import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.CreateCompositionRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.UpdateCompositionRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response.CompositionResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicComposition;
import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicTrack;
import com.tobi.MusicLearn_Studio_Backend.modules.music.repository.MusicCompositionRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.music.repository.MusicTrackRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.music.service.MusicCompositionService;
import com.tobi.MusicLearn_Studio_Backend.common.service.R2StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MusicCompositionServiceImpl implements MusicCompositionService {

    private final MusicCompositionRepository compositionRepository;
    private final MusicTrackRepository musicTrackRepository;
    private final R2StorageService r2StorageService;

    @Override
    public CompositionResponse createComposition(CreateCompositionRequest request, String userId) {
        log.info("Creating composition for user: {}", userId);

        MusicComposition composition = MusicComposition.builder()
                .userId(userId)
                .title(request.getTitle())
                .description(request.getDescription())
                .status(MusicComposition.CompositionStatus.DRAFT)
                .bpm(request.getBpm())
                .key(request.getKey())
                .scale(request.getScale())
                .tracks(request.getTracks())
                .isPublic(request.getIsPublic())
                .build();

        MusicComposition saved = compositionRepository.save(composition);
        log.info("Composition created with ID: {}", saved.getId());

        return CompositionResponse.fromEntity(saved);
    }

    @Override
    public CompositionResponse getOrCreateDraft(String userId) {
        log.info("Getting or creating draft for user: {}", userId);

        // Check if user already has a draft
        List<MusicComposition> drafts = compositionRepository.findByUserIdAndStatus(userId,
                MusicComposition.CompositionStatus.DRAFT);

        if (!drafts.isEmpty()) {
            // Return the most recent draft
            // If multiple exist (legacy data), return the first one
            return CompositionResponse.fromEntity(drafts.get(0));
        }

        // Create new draft
        MusicComposition composition = MusicComposition.builder()
                .userId(userId)
                .title("Untitled Composition")
                .description("New composition")
                .status(MusicComposition.CompositionStatus.DRAFT)
                .bpm(120)
                .key("C")
                .scale("Major")
                .isPublic(false)
                .build();

        MusicComposition saved = compositionRepository.save(composition);
        log.info("New draft created with ID: {}", saved.getId());

        return CompositionResponse.fromEntity(saved);
    }

    @Override
    public CompositionResponse updateComposition(String compositionId, UpdateCompositionRequest request,
            String userId) {
        log.info("Updating composition: {} for user: {}", compositionId, userId);

        MusicComposition composition = compositionRepository.findById(compositionId)
                .orElseThrow(() -> new RuntimeException("Composition not found"));

        if (!composition.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this composition");
        }

        if (request.getTitle() != null) {
            composition.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            composition.setDescription(request.getDescription());
        }
        if (request.getBpm() != null) {
            composition.setBpm(request.getBpm());
        }
        if (request.getKey() != null) {
            composition.setKey(request.getKey());
        }
        if (request.getScale() != null) {
            composition.setScale(request.getScale());
        }
        if (request.getTracks() != null) {
            composition.setTracks(request.getTracks());
        }
        if (request.getIsPublic() != null) {
            composition.setIsPublic(request.getIsPublic());
        }
        if (request.getCoverImageUrl() != null) {
            composition.setCoverImageUrl(request.getCoverImageUrl());
        }

        composition.setUpdatedAt(LocalDateTime.now());

        MusicComposition updated = compositionRepository.save(composition);
        log.info("Composition updated: {}", updated.getId());

        return CompositionResponse.fromEntity(updated);
    }

    @Override
    public CompositionResponse getCompositionById(String compositionId, String userId) {
        log.info("Getting composition: {} for user: {}", compositionId, userId);

        MusicComposition composition = compositionRepository.findById(compositionId)
                .orElseThrow(() -> new RuntimeException("Composition not found"));

        // Check access permissions
        if (!composition.getUserId().equals(userId) &&
                !(composition.getStatus() == MusicComposition.CompositionStatus.PUBLISHED
                        && composition.getIsPublic())) {
            throw new RuntimeException("Unauthorized to access this composition");
        }

        return CompositionResponse.fromEntity(composition);
    }

    @Override
    public List<CompositionResponse> getUserCompositions(String userId) {
        log.info("Getting all compositions for user: {}", userId);

        return compositionRepository.findByUserIdOrderByUpdatedAtDesc(userId).stream()
                .map(CompositionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompositionResponse> getUserDrafts(String userId) {
        log.info("Getting drafts for user: {}", userId);

        return compositionRepository.findByUserIdAndStatus(userId, MusicComposition.CompositionStatus.DRAFT).stream()
                .map(CompositionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompositionResponse> getUserPublishedCompositions(String userId) {
        log.info("Getting published compositions for user: {}", userId);

        return compositionRepository.findByUserIdAndStatus(userId, MusicComposition.CompositionStatus.PUBLISHED)
                .stream()
                .map(CompositionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompositionResponse> getPublicCompositions() {
        log.info("Getting public compositions");

        return compositionRepository.findByStatusAndIsPublicOrderByCreatedAtDesc(
                MusicComposition.CompositionStatus.PUBLISHED,
                true).stream()
                .map(CompositionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CompositionResponse publishComposition(String compositionId, MultipartFile audioFile, String userId)
            throws IOException {
        log.info("Publishing composition: {} for user: {}", compositionId, userId);

        MusicComposition composition = compositionRepository.findById(compositionId)
                .orElseThrow(() -> new RuntimeException("Composition not found"));

        if (!composition.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to publish this composition");
        }

        if (composition.getStatus() == MusicComposition.CompositionStatus.PUBLISHED) {
            throw new RuntimeException("Composition is already published");
        }

        // Upload audio file to R2
        String audioUrl = r2StorageService.uploadFile(audioFile, "music/compositions");

        composition.setAudioFileUrl(audioUrl);
        composition.setStatus(MusicComposition.CompositionStatus.PUBLISHED);
        composition.setFileSize(audioFile.getSize());
        composition.setUpdatedAt(LocalDateTime.now());

        MusicComposition published = compositionRepository.save(composition);
        log.info("Composition published: {}", published.getId());

        // Also create a MusicTrack entry so it appears in music listings
        try {
            // Calculate duration from file (estimate - you might want to calculate this
            // better)
            double estimatedDuration = calculateDuration(published);

            MusicTrack track = MusicTrack.builder()
                    .userId(userId)
                    .title(published.getTitle())
                    .description(published.getDescription())
                    .fileUrl(audioUrl)
                    .coverImageUrl(published.getCoverImageUrl())
                    .thumbnailUrl(published.getCoverImageUrl())
                    .duration(estimatedDuration)
                    .fileSize(audioFile.getSize())
                    .genre("Composition")
                    .tags(List.of("composition", "studio"))
                    .isPublic(published.getIsPublic())
                    .playCount(0L)
                    .likeCount(0L)
                    .commentCount(0L)
                    .build();

            musicTrackRepository.save(track);
            log.info("Created MusicTrack entry for published composition: {}", compositionId);
        } catch (Exception e) {
            log.error("Failed to create MusicTrack for composition", e);
            // Don't fail the entire publish operation
        }

        return CompositionResponse.fromEntity(published);
    }

    private double calculateDuration(MusicComposition composition) {
        // Estimate duration based on number of notes and BPM
        // This is a rough estimate - adjust as needed
        if (composition.getTracks() == null || composition.getTracks().isEmpty()) {
            return 1.0; // Default 1 minute
        }

        // Calculate based on BPM and total beats
        int totalBeats = 16; // Default for now
        int bpm = composition.getBpm() != null ? composition.getBpm() : 120;
        double durationSeconds = (totalBeats / (double) bpm) * 60;
        return durationSeconds / 60.0; // Convert to minutes
    }

    @Override
    public void deleteComposition(String compositionId, String userId) {
        log.info("Deleting composition: {} for user: {}", compositionId, userId);

        MusicComposition composition = compositionRepository.findById(compositionId)
                .orElseThrow(() -> new RuntimeException("Composition not found"));

        if (!composition.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this composition");
        }

        // Delete audio file from R2 if exists
        if (composition.getAudioFileUrl() != null) {
            try {
                String fileName = composition.getAudioFileUrl().substring(
                        composition.getAudioFileUrl().lastIndexOf("/") + 1);
                r2StorageService.deleteFile(fileName);
            } catch (Exception e) {
                log.error("Failed to delete audio file from R2", e);
            }
        }

        compositionRepository.delete(composition);
        log.info("Composition deleted: {}", compositionId);
    }

    @Override
    public CompositionResponse duplicateComposition(String compositionId, String userId) {
        log.info("Duplicating composition: {} for user: {}", compositionId, userId);

        MusicComposition original = compositionRepository.findById(compositionId)
                .orElseThrow(() -> new RuntimeException("Composition not found"));

        if (!original.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to duplicate this composition");
        }

        MusicComposition duplicate = MusicComposition.builder()
                .userId(userId)
                .title(original.getTitle() + " (Copy)")
                .description(original.getDescription())
                .status(MusicComposition.CompositionStatus.DRAFT)
                .bpm(original.getBpm())
                .key(original.getKey())
                .scale(original.getScale())
                .tracks(original.getTracks())
                .isPublic(false) // Reset to private
                .build();

        MusicComposition saved = compositionRepository.save(duplicate);
        log.info("Composition duplicated with new ID: {}", saved.getId());

        return CompositionResponse.fromEntity(saved);
    }
}
