package com.tobi.MusicLearn_Studio_Backend.modules.learning.repository;

import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.Chapter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends MongoRepository<Chapter, String> {

    List<Chapter> findByCourseIdOrderByOrderIndexAsc(String courseId);

    List<Chapter> findByCourseIdAndIsPublishedOrderByOrderIndexAsc(String courseId, Boolean isPublished);

    Optional<Chapter> findBySlug(String slug);

    boolean existsBySlug(String slug);

    long countByCourseId(String courseId);

    void deleteByCourseId(String courseId);
}
