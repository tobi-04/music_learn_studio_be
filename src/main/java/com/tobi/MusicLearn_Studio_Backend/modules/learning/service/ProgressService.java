package com.tobi.MusicLearn_Studio_Backend.modules.learning.service;

import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.StudentStatsResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.UserCourseProgressResponse;

import java.util.List;

public interface ProgressService {

    /**
     * Enroll a user in a course
     */
    UserCourseProgressResponse enrollCourse(String userId, String courseId);

    /**
     * Enroll user after successful payment (bypasses price validation)
     */
    UserCourseProgressResponse enrollCourseAfterPayment(String userId, String courseId);

    /**
     * Mark a chapter as completed
     */
    void markChapterComplete(String userId, String chapterId);

    /**
     * Get user's progress for a specific course
     */
    UserCourseProgressResponse getCourseProgress(String userId, String courseId);

    /**
     * Get all courses progress for a user
     */
    List<UserCourseProgressResponse> getAllUserProgress(String userId);

    /**
     * Get student statistics
     */
    StudentStatsResponse getStudentStats(String userId);

    /**
     * Calculate and update course progress percentage
     */
    void recalculateCourseProgress(String userId, String courseId);
}
