package com.tobi.MusicLearn_Studio_Backend.modules.music.service;

import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.CreateMusicTrackRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.UpdateMusicTrackRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response.MusicTrackResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MusicTrackService {

    /**
     * Upload a new music track with file
     */
    MusicTrackResponse uploadTrack(MultipartFile file, MultipartFile image, CreateMusicTrackRequest request,
            String userId)
            throws IOException;

    /**
     * Get all trending tracks (public tracks ordered by play count)
     */
    List<MusicTrackResponse> getTrendingTracks(String genre);

    /**
     * Get tracks uploaded by the current user
     */
    List<MusicTrackResponse> getMyTracks(String userId);

    /**
     * Get recently listened tracks for the current user (limit 10)
     */
    List<MusicTrackResponse> getRecentListeningHistory(String userId);

    /**
     * Record a listening event when user plays a track
     */
    void recordListening(String userId, String trackId);

    /**
     * Delete a track (only owner can delete)
     */
    void deleteTrack(String trackId, String userId);

    /**
     * Update track metadata (title, description, genre, tags, cover image)
     * Does not allow updating the audio file itself
     */
    MusicTrackResponse updateTrack(String trackId, MultipartFile image,
            UpdateMusicTrackRequest request, String userId) throws IOException;

    /**
     * Get track by ID (only if public or owned by user)
     */
    MusicTrackResponse getTrackById(String trackId, String userId);

    /**
     * Increment play count
     */
    void incrementPlayCount(String trackId);
}
