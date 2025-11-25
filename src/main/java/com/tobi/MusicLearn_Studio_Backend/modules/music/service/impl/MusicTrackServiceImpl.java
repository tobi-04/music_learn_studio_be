package com.tobi.MusicLearn_Studio_Backend.modules.music.service.impl;

import com.tobi.MusicLearn_Studio_Backend.common.service.R2StorageService;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.entity.User;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.repository.UserRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.CreateMusicTrackRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response.MusicTrackResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicListeningHistory;
import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicTrack;
import com.tobi.MusicLearn_Studio_Backend.modules.music.repository.MusicListeningHistoryRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.music.repository.MusicTrackRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.music.service.MusicTrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MusicTrackServiceImpl implements MusicTrackService {

    private final MusicTrackRepository musicTrackRepository;
    private final MusicListeningHistoryRepository listeningHistoryRepository;
    private final UserRepository userRepository;
    private final R2StorageService r2StorageService;

    @Override
    @Transactional
    public MusicTrackResponse uploadTrack(MultipartFile file, CreateMusicTrackRequest request, String userId)
            throws IOException {
        // Validate audio file
        if (!r2StorageService.validateAudioFile(file)) {
            throw new IllegalArgumentException("Invalid audio file. Only MP3 files up to 50MB are allowed.");
        }

        // Upload file to R2
        String fileUrl = r2StorageService.uploadFile(file, "music/tracks");

        // Create track entity
        MusicTrack track = MusicTrack.builder()
                .userId(userId)
                .title(request.getTitle())
                .description(request.getDescription())
                .fileUrl(fileUrl)
                .coverImageUrl(request.getCoverImageUrl())
                .duration(request.getDuration())
                .fileSize(request.getFileSize())
                .genre(request.getGenre())
                .tags(request.getTags())
                .isPublic(request.getIsPublic())
                .playCount(0L)
                .likeCount(0L)
                .commentCount(0L)
                .build();

        track = musicTrackRepository.save(track);

        log.info("Music track uploaded: {} by user {}", track.getTitle(), userId);

        return toResponse(track);
    }

    @Override
    public List<MusicTrackResponse> getTrendingTracks(String genre) {
        List<MusicTrack> tracks;
        if (genre != null && !genre.isEmpty()) {
            tracks = musicTrackRepository.findByIsPublicTrueAndGenreOrderByPlayCountDesc(genre);
        } else {
            tracks = musicTrackRepository.findByIsPublicTrueOrderByPlayCountDesc();
        }
        return tracks.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MusicTrackResponse> getMyTracks(String userId) {
        List<MusicTrack> tracks = musicTrackRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return tracks.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MusicTrackResponse> getRecentListeningHistory(String userId) {
        // Get recent 10 listening history records
        List<MusicListeningHistory> history = listeningHistoryRepository
                .findByUserIdOrderByListenedAtDesc(userId, PageRequest.of(0, 10));

        // Use LinkedHashSet to preserve order while removing duplicates
        Set<String> trackIds = new LinkedHashSet<>();
        for (MusicListeningHistory record : history) {
            trackIds.add(record.getTrackId());
        }

        // Fetch tracks
        List<MusicTrackResponse> tracks = new ArrayList<>();
        for (String trackId : trackIds) {
            musicTrackRepository.findById(trackId).ifPresent(track -> {
                if (track.getIsPublic()) {
                    tracks.add(toResponse(track));
                }
            });
        }

        return tracks;
    }

    @Override
    @Transactional
    public void recordListening(String userId, String trackId) {
        // Verify track exists and is public
        MusicTrack track = musicTrackRepository.findByIdAndIsPublicTrue(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found or not public"));

        // Create listening record
        MusicListeningHistory history = MusicListeningHistory.builder()
                .userId(userId)
                .trackId(trackId)
                .listenedAt(LocalDateTime.now())
                .build();

        listeningHistoryRepository.save(history);

        // Increment play count
        track.setPlayCount(track.getPlayCount() + 1);
        musicTrackRepository.save(track);

        log.info("Recorded listening: User {} played track {}", userId, trackId);
    }

    @Override
    @Transactional
    public void deleteTrack(String trackId, String userId) {
        // Find track and verify ownership
        MusicTrack track = musicTrackRepository.findByIdAndUserId(trackId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Track not found or you don't have permission to delete it"));

        // Delete file from R2
        if (track.getFileUrl() != null) {
            r2StorageService.deleteFile(track.getFileUrl());
        }

        // Delete track
        musicTrackRepository.delete(track);

        log.info("Deleted track: {} by user {}", trackId, userId);
    }

    @Override
    public MusicTrackResponse getTrackById(String trackId, String userId) {
        MusicTrack track = musicTrackRepository.findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found"));

        // Check if user can access this track (public or owned by user)
        if (!track.getIsPublic() && !track.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to access this track");
        }

        return toResponse(track);
    }

    @Override
    @Transactional
    public void incrementPlayCount(String trackId) {
        musicTrackRepository.findById(trackId).ifPresent(track -> {
            track.setPlayCount(track.getPlayCount() + 1);
            musicTrackRepository.save(track);
        });
    }

    private MusicTrackResponse toResponse(MusicTrack track) {
        // Fetch user info
        User user = userRepository.findById(track.getUserId()).orElse(null);

        return MusicTrackResponse.builder()
                .id(track.getId())
                .userId(track.getUserId())
                .username(user != null ? user.getUsername() : "Unknown")
                .userAvatar(user != null ? user.getAvatar() : null)
                .title(track.getTitle())
                .description(track.getDescription())
                .fileUrl(track.getFileUrl())
                .coverImageUrl(track.getCoverImageUrl())
                .duration(track.getDuration())
                .fileSize(track.getFileSize())
                .waveformData(track.getWaveformData())
                .genre(track.getGenre())
                .tags(track.getTags())
                .playCount(track.getPlayCount())
                .likeCount(track.getLikeCount())
                .commentCount(track.getCommentCount())
                .isPublic(track.getIsPublic())
                .createdAt(track.getCreatedAt())
                .updatedAt(track.getUpdatedAt())
                .build();
    }
}
