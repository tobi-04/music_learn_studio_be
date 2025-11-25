package com.tobi.MusicLearn_Studio_Backend.modules.enrollment.service;

import com.tobi.MusicLearn_Studio_Backend.modules.enrollment.entity.Enrollment;

import java.util.List;
import java.util.Map;

public interface EnrollmentService {

    Enrollment createEnrollment(String userId, String courseId);

    Enrollment getEnrollmentById(String id);

    Enrollment getEnrollmentByUserAndCourse(String userId, String courseId);

    List<Enrollment> getEnrollmentsByUserId(String userId);

    List<Enrollment> getEnrollmentsByCourseId(String courseId);

    boolean isUserEnrolled(String userId, String courseId);

    Long getTotalStudents();

    Long getActiveEnrollments();

    Long getEnrollmentCountByCourse(String courseId);

    Map<String, Long> getMonthlyStudentGrowth(int months);

    void updateEnrollmentProgress(String enrollmentId, Integer progress);

    void completeEnrollment(String enrollmentId);
}
