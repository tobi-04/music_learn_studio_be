package com.tobi.MusicLearn_Studio_Backend.modules.music.repository;

import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicLike;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicLikeRepository extends MongoRepository<MusicLike, String> {

    Optional<MusicLike> findByUserIdAndTrackId(String userId, String trackId);

    List<MusicLike> findByTrackId(String trackId);

    List<MusicLike> findByUserId(String userId);

    long countByTrackId(String trackId);

    boolean existsByUserIdAndTrackId(String userId, String trackId);

    void deleteByUserIdAndTrackId(String userId, String trackId);
}
