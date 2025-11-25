package com.tobi.MusicLearn_Studio_Backend.modules.learning.repository;

import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {

    Optional<Course> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Page<Course> findByIsPublished(Boolean isPublished, Pageable pageable);

    Page<Course> findByLevel(String level, Pageable pageable);

    Page<Course> findByIsPublishedAndLevel(Boolean isPublished, String level, Pageable pageable);

    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
