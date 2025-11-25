package com.tobi.MusicLearn_Studio_Backend.modules.music.repository;

import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicTrack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicTrackRepository extends MongoRepository<MusicTrack, String> {

    /**
     * Find all public tracks ordered by play count (trending)
     */
    List<MusicTrack> findByIsPublicTrueOrderByPlayCountDesc();

    /**
     * Find all public tracks by genre ordered by play count
     */
    List<MusicTrack> findByIsPublicTrueAndGenreOrderByPlayCountDesc(String genre);

    /**
     * Find all tracks by user
     */
    List<MusicTrack> findByUserIdOrderByCreatedAtDesc(String userId);

    /**
     * Find a track by ID only if it's public
     */
    Optional<MusicTrack> findByIdAndIsPublicTrue(String id);

    /**
     * Find track by ID and user (for ownership verification)
     */
    Optional<MusicTrack> findByIdAndUserId(String id, String userId);

    /**
     * Get paginated public tracks
     */
    Page<MusicTrack> findByIsPublicTrue(Pageable pageable);
}
