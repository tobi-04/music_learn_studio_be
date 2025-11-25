package com.tobi.MusicLearn_Studio_Backend.modules.music.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.CreateCommentRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response.CommentResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.music.service.MusicLikeCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/music/tracks")
@RequiredArgsConstructor
@Tag(name = "Music Likes & Comments", description = "APIs for track likes and comments")
public class MusicLikeCommentController {

    private final MusicLikeCommentService likeCommentService;

    @PostMapping("/{trackId}/like")
    @Operation(summary = "Toggle like on a track")
    public ResponseEntity<BaseResponse<Map<String, Object>>> toggleLike(
            @PathVariable String trackId,
            @RequestHeader("X-User-Id") String userId) {

        boolean liked = likeCommentService.toggleLike(userId, trackId);
        long likeCount = likeCommentService.getLikeCount(trackId);

        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("likeCount", likeCount);

        return ResponseEntity.ok(BaseResponse.<Map<String, Object>>builder()
                .success(true)
                .message(liked ? "Track liked" : "Track unliked")
                .data(data)
                .build());
    }

    @GetMapping("/{trackId}/like-status")
    @Operation(summary = "Check if user has liked a track")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getLikeStatus(
            @PathVariable String trackId,
            @RequestHeader("X-User-Id") String userId) {

        boolean liked = likeCommentService.hasUserLiked(userId, trackId);
        long likeCount = likeCommentService.getLikeCount(trackId);

        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("likeCount", likeCount);

        return ResponseEntity.ok(BaseResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Like status retrieved")
                .data(data)
                .build());
    }

    @GetMapping("/liked")
    @Operation(summary = "Get all tracks liked by user")
    public ResponseEntity<BaseResponse<List<String>>> getLikedTracks(
            @RequestHeader("X-User-Id") String userId) {

        List<String> trackIds = likeCommentService.getLikedTracksByUser(userId);

        return ResponseEntity.ok(BaseResponse.<List<String>>builder()
                .success(true)
                .message("Liked tracks retrieved")
                .data(trackIds)
                .build());
    }

    @PostMapping("/{trackId}/comments")
    @Operation(summary = "Add a comment to a track")
    public ResponseEntity<BaseResponse<CommentResponse>> addComment(
            @PathVariable String trackId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CreateCommentRequest request) {

        CommentResponse comment = likeCommentService.addComment(userId, trackId, request);

        return ResponseEntity.ok(BaseResponse.<CommentResponse>builder()
                .success(true)
                .message("Comment added successfully")
                .data(comment)
                .build());
    }

    @GetMapping("/{trackId}/comments")
    @Operation(summary = "Get all comments for a track")
    public ResponseEntity<BaseResponse<List<CommentResponse>>> getComments(
            @PathVariable String trackId) {

        List<CommentResponse> comments = likeCommentService.getCommentsByTrack(trackId);

        return ResponseEntity.ok(BaseResponse.<List<CommentResponse>>builder()
                .success(true)
                .message("Comments retrieved successfully")
                .data(comments)
                .build());
    }

    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "Delete a comment")
    public ResponseEntity<BaseResponse<Void>> deleteComment(
            @PathVariable String commentId,
            @RequestHeader("X-User-Id") String userId) {

        likeCommentService.deleteComment(commentId, userId);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .success(true)
                .message("Comment deleted successfully")
                .build());
    }
}
