package com.tobi.MusicLearn_Studio_Backend.modules.enrollment.repository;

import com.tobi.MusicLearn_Studio_Backend.modules.enrollment.entity.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {

    Optional<Enrollment> findByUserIdAndCourseId(String userId, String courseId);

    List<Enrollment> findByUserId(String userId);

    List<Enrollment> findByCourseId(String courseId);

    Long countByCourseId(String courseId);

    @Query("{ 'isActive': true }")
    Long countActiveEnrollments();

    @Query("{ 'completedAt': { $ne: null } }")
    Long countCompletedEnrollments();

    boolean existsByUserIdAndCourseId(String userId, String courseId);

    @Query("{ 'enrolledAt': { $gte: ?0, $lte: ?1 } }")
    List<Enrollment> findEnrollmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{ }")
    List<Enrollment> findAllEnrollments();
}
