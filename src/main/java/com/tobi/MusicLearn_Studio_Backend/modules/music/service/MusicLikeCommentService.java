package com.tobi.MusicLearn_Studio_Backend.modules.music.service;

import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.CreateCommentRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response.CommentResponse;

import java.util.List;

public interface MusicLikeCommentService {

    /**
     * Toggle like on a track (like if not liked, unlike if already liked)
     */
    boolean toggleLike(String userId, String trackId);

    /**
     * Check if user has liked a track
     */
    boolean hasUserLiked(String userId, String trackId);

    /**
     * Get like count for a track
     */
    long getLikeCount(String trackId);

    /**
     * Get all tracks liked by user
     */
    List<String> getLikedTracksByUser(String userId);

    /**
     * Add a comment to a track
     */
    CommentResponse addComment(String userId, String trackId, CreateCommentRequest request);

    /**
     * Get all comments for a track
     */
    List<CommentResponse> getCommentsByTrack(String trackId);

    /**
     * Delete a comment (only by owner)
     */
    void deleteComment(String commentId, String userId);

    /**
     * Get comment count for a track
     */
    long getCommentCount(String trackId);
}
