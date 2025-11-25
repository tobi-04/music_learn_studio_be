package com.tobi.MusicLearn_Studio_Backend.modules.music.repository;

import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicListeningHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicListeningHistoryRepository extends MongoRepository<MusicListeningHistory, String> {

    /**
     * Find recent listening history for a user
     */
    List<MusicListeningHistory> findByUserIdOrderByListenedAtDesc(String userId, Pageable pageable);

    /**
     * Find if user has listened to a track recently (for deduplication)
     */
    List<MusicListeningHistory> findByUserIdAndTrackIdOrderByListenedAtDesc(String userId, String trackId);
}
