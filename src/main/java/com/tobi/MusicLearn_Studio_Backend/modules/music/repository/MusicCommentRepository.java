package com.tobi.MusicLearn_Studio_Backend.modules.music.repository;

import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicCommentRepository extends MongoRepository<MusicComment, String> {

    List<MusicComment> findByTrackIdOrderByCreatedAtDesc(String trackId);

    List<MusicComment> findByUserIdOrderByCreatedAtDesc(String userId);

    long countByTrackId(String trackId);
}
