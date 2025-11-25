package com.tobi.MusicLearn_Studio_Backend.modules.learning.repository;

import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.progress.UserCourseProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseProgressRepository extends MongoRepository<UserCourseProgress, String> {

    Optional<UserCourseProgress> findByUserIdAndCourseId(String userId, String courseId);

    List<UserCourseProgress> findByUserId(String userId);

    List<UserCourseProgress> findByCourseId(String courseId);

    boolean existsByUserIdAndCourseId(String userId, String courseId);
}
