package com.tobi.MusicLearn_Studio_Backend.modules.music.repository;

import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicComposition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicCompositionRepository extends MongoRepository<MusicComposition, String> {

    List<MusicComposition> findByUserIdOrderByUpdatedAtDesc(String userId);

    List<MusicComposition> findByUserIdAndStatus(String userId, MusicComposition.CompositionStatus status);

    List<MusicComposition> findByStatusAndIsPublicOrderByCreatedAtDesc(
            MusicComposition.CompositionStatus status,
            Boolean isPublic);
}
