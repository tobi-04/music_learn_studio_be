package com.tobi.MusicLearn_Studio_Backend.modules.music.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.CreateMusicTrackRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.UpdateMusicTrackRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response.MusicTrackResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.music.service.MusicTrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/music/tracks")
@RequiredArgsConstructor
@Tag(name = "Music Tracks", description = "APIs for music track management")
public class MusicTrackController {

    private final MusicTrackService musicTrackService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a new music track")
    public ResponseEntity<BaseResponse<MusicTrackResponse>> uploadTrack(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @Valid @RequestPart("data") CreateMusicTrackRequest request,
            @RequestHeader("X-User-Id") String userId) throws IOException {

        MusicTrackResponse response = musicTrackService.uploadTrack(file, image, request, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<MusicTrackResponse>builder()
                        .success(true)
                        .message("Track uploaded successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/trending")
    @Operation(summary = "Get trending tracks (public tracks ordered by play count)")
    public ResponseEntity<BaseResponse<List<MusicTrackResponse>>> getTrendingTracks(
            @RequestParam(required = false) String genre) {
        List<MusicTrackResponse> tracks = musicTrackService.getTrendingTracks(genre);

        return ResponseEntity.ok(BaseResponse.<List<MusicTrackResponse>>builder()
                .success(true)
                .message("Trending tracks retrieved successfully")
                .data(tracks)
                .build());
    }

    @GetMapping("/my-tracks")
    @Operation(summary = "Get my uploaded tracks")
    public ResponseEntity<BaseResponse<List<MusicTrackResponse>>> getMyTracks(
            @RequestHeader("X-User-Id") String userId) {

        List<MusicTrackResponse> tracks = musicTrackService.getMyTracks(userId);

        return ResponseEntity.ok(BaseResponse.<List<MusicTrackResponse>>builder()
                .success(true)
                .message("Your tracks retrieved successfully")
                .data(tracks)
                .build());
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recently listened tracks (last 10)")
    public ResponseEntity<BaseResponse<List<MusicTrackResponse>>> getRecentListeningHistory(
            @RequestHeader("X-User-Id") String userId) {

        List<MusicTrackResponse> tracks = musicTrackService.getRecentListeningHistory(userId);

        return ResponseEntity.ok(BaseResponse.<List<MusicTrackResponse>>builder()
                .success(true)
                .message("Recent listening history retrieved successfully")
                .data(tracks)
                .build());
    }

    @PostMapping("/{trackId}/listen")
    @Operation(summary = "Record a listening event when user plays a track")
    public ResponseEntity<BaseResponse<Void>> recordListening(
            @PathVariable String trackId,
            @RequestHeader("X-User-Id") String userId) {

        musicTrackService.recordListening(userId, trackId);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .success(true)
                .message("Listening event recorded successfully")
                .build());
    }

    @DeleteMapping("/{trackId}")
    @Operation(summary = "Delete a track (only owner can delete)")
    public ResponseEntity<BaseResponse<Void>> deleteTrack(
            @PathVariable String trackId,
            @RequestHeader("X-User-Id") String userId) {

        musicTrackService.deleteTrack(trackId, userId);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .success(true)
                .message("Track deleted successfully")
                .build());
    }

    @PutMapping(value = "/{trackId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update track metadata (title, description, genre, tags, cover image)")
    public ResponseEntity<BaseResponse<MusicTrackResponse>> updateTrack(
            @PathVariable String trackId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @Valid @RequestPart("data") UpdateMusicTrackRequest request,
            @RequestHeader("X-User-Id") String userId) throws IOException {

        MusicTrackResponse response = musicTrackService.updateTrack(trackId, image, request, userId);

        return ResponseEntity.ok(BaseResponse.<MusicTrackResponse>builder()
                .success(true)
                .message("Track updated successfully")
                .data(response)
                .build());
    }

    @GetMapping("/{trackId}")
    @Operation(summary = "Get a track by ID")
    public ResponseEntity<BaseResponse<MusicTrackResponse>> getTrackById(
            @PathVariable String trackId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {

        MusicTrackResponse track = musicTrackService.getTrackById(trackId, userId);

        return ResponseEntity.ok(BaseResponse.<MusicTrackResponse>builder()
                .success(true)
                .message("Track retrieved successfully")
                .data(track)
                .build());
    }
}
