package com.tobi.MusicLearn_Studio_Backend.modules.enrollment.service.impl;

import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import com.tobi.MusicLearn_Studio_Backend.modules.enrollment.entity.Enrollment;
import com.tobi.MusicLearn_Studio_Backend.modules.enrollment.repository.EnrollmentRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.enrollment.service.EnrollmentService;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.Course;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public Enrollment createEnrollment(String userId, String courseId) {
        // Check if already enrolled
        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new IllegalStateException("User is already enrolled in this course");
        }

        // Create enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setCourseId(courseId);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setProgress(0);
        enrollment.setIsActive(true);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Update course student count
        updateCourseStudentCount(courseId);

        return savedEnrollment;
    }

    @Override
    public Enrollment getEnrollmentById(String id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
    }

    @Override
    public Enrollment getEnrollmentByUserAndCourse(String userId, String courseId) {
        return enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for user and course"));
    }

    @Override
    public List<Enrollment> getEnrollmentsByUserId(String userId) {
        return enrollmentRepository.findByUserId(userId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourseId(String courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    @Override
    public boolean isUserEnrolled(String userId, String courseId) {
        return enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    @Override
    public Long getTotalStudents() {
        List<Enrollment> allEnrollments = enrollmentRepository.findAllEnrollments();
        return allEnrollments.stream()
                .map(Enrollment::getUserId)
                .distinct()
                .count();
    }

    @Override
    public Long getActiveEnrollments() {
        return enrollmentRepository.countActiveEnrollments();
    }

    @Override
    public Long getEnrollmentCountByCourse(String courseId) {
        return enrollmentRepository.countByCourseId(courseId);
    }

    @Override
    public Map<String, Long> getMonthlyStudentGrowth(int months) {
        Map<String, Long> monthlyGrowth = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

        for (int i = months - 1; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.now().minusMonths(i);
            LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            // Count unique users up to this month
            List<Enrollment> cumulativeEnrollments = enrollmentRepository.findEnrollmentsBetweenDates(
                    LocalDateTime.of(2020, 1, 1, 0, 0), endOfMonth);

            long uniqueUsers = cumulativeEnrollments.stream()
                    .map(Enrollment::getUserId)
                    .distinct()
                    .count();

            monthlyGrowth.put(yearMonth.format(formatter), uniqueUsers);
        }

        return monthlyGrowth;
    }

    @Override
    @Transactional
    public void updateEnrollmentProgress(String enrollmentId, Integer progress) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        enrollment.setProgress(progress);
        enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional
    public void completeEnrollment(String enrollmentId) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        enrollment.setCompletedAt(LocalDateTime.now());
        enrollment.setProgress(100);
        enrollmentRepository.save(enrollment);
    }

    private void updateCourseStudentCount(String courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course != null) {
            Long count = enrollmentRepository.countByCourseId(courseId);
            course.setTotalStudents(count);
            courseRepository.save(course);
        }
    }
}
