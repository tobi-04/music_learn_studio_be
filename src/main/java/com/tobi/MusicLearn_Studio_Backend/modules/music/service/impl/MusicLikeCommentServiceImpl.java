package com.tobi.MusicLearn_Studio_Backend.modules.music.service.impl;

import com.tobi.MusicLearn_Studio_Backend.common.exceptions.BadRequestException;
import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.entity.User;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.repository.UserRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.CreateCommentRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response.CommentResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicComment;
import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicLike;
import com.tobi.MusicLearn_Studio_Backend.modules.music.repository.MusicCommentRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.music.repository.MusicLikeRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.music.repository.MusicTrackRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.music.service.MusicLikeCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MusicLikeCommentServiceImpl implements MusicLikeCommentService {

    private final MusicLikeRepository likeRepository;
    private final MusicCommentRepository commentRepository;
    private final MusicTrackRepository trackRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean toggleLike(String userId, String trackId) {
        log.info("Toggling like for track: {} by user: {}", trackId, userId);

        // Verify track exists
        trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        Optional<MusicLike> existingLike = likeRepository.findByUserIdAndTrackId(userId, trackId);

        if (existingLike.isPresent()) {
            // Unlike
            likeRepository.delete(existingLike.get());
            log.info("User {} unliked track {}", userId, trackId);
            return false;
        } else {
            // Like
            MusicLike like = MusicLike.builder()
                    .userId(userId)
                    .trackId(trackId)
                    .createdAt(LocalDateTime.now())
                    .build();
            likeRepository.save(like);
            log.info("User {} liked track {}", userId, trackId);
            return true;
        }
    }

    @Override
    public boolean hasUserLiked(String userId, String trackId) {
        return likeRepository.existsByUserIdAndTrackId(userId, trackId);
    }

    @Override
    public long getLikeCount(String trackId) {
        return likeRepository.countByTrackId(trackId);
    }

    @Override
    public List<String> getLikedTracksByUser(String userId) {
        return likeRepository.findByUserId(userId).stream()
                .map(MusicLike::getTrackId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponse addComment(String userId, String trackId, CreateCommentRequest request) {
        log.info("Adding comment to track: {} by user: {}", trackId, userId);

        // Verify track exists
        trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        // Create comment
        MusicComment comment = MusicComment.builder()
                .userId(userId)
                .trackId(trackId)
                .content(request.getContent())
                .build();

        MusicComment saved = commentRepository.save(comment);
        log.info("Comment added successfully: {}", saved.getId());

        return mapToCommentResponse(saved);
    }

    @Override
    public List<CommentResponse> getCommentsByTrack(String trackId) {
        List<MusicComment> comments = commentRepository.findByTrackIdOrderByCreatedAtDesc(trackId);

        return comments.stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(String commentId, String userId) {
        log.info("Deleting comment: {} by user: {}", commentId, userId);

        MusicComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // Verify ownership
        if (!comment.getUserId().equals(userId)) {
            throw new BadRequestException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
        log.info("Comment deleted successfully: {}", commentId);
    }

    @Override
    public long getCommentCount(String trackId) {
        return commentRepository.countByTrackId(trackId);
    }

    private CommentResponse mapToCommentResponse(MusicComment comment) {
        User user = userRepository.findById(comment.getUserId()).orElse(null);

        return CommentResponse.builder()
                .id(comment.getId())
                .trackId(comment.getTrackId())
                .userId(comment.getUserId())
                .username(user != null ? user.getUsername() : "Unknown")
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
