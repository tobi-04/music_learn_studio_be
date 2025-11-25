package com.tobi.MusicLearn_Studio_Backend.modules.learning.repository;

import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.progress.UserChapterProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChapterProgressRepository extends MongoRepository<UserChapterProgress, String> {

    Optional<UserChapterProgress> findByUserIdAndChapterId(String userId, String chapterId);

    List<UserChapterProgress> findByUserId(String userId);

    List<UserChapterProgress> findByChapterId(String chapterId);

    long countByUserIdAndIsCompleted(String userId, Boolean isCompleted);
}
