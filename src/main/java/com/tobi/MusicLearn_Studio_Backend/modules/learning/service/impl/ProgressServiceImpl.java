package com.tobi.MusicLearn_Studio_Backend.modules.learning.service.impl;

import com.tobi.MusicLearn_Studio_Backend.common.exceptions.BadRequestException;
import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.AdminProgressStatsResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.CourseProgressStatsResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.StudentResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.StudentStatsResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.UserCourseProgressResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.Chapter;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.Course;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.progress.UserChapterProgress;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.progress.UserCourseProgress;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.repository.*;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.service.ProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressServiceImpl implements ProgressService {

    private final UserCourseProgressRepository userCourseProgressRepository;
    private final UserChapterProgressRepository userChapterProgressRepository;
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final com.tobi.MusicLearn_Studio_Backend.modules.auth.repository.UserRepository userRepository;

    @Override
    @Transactional
    public UserCourseProgressResponse enrollCourse(String userId, String courseId) {
        // Verify course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        // Check if this is a paid course
        if (course.getPrice() != null && course.getPrice() > 0) {
            throw new BadRequestException("This is a paid course. Please complete payment first.");
        }

        return performEnrollment(userId, courseId, course);
    }

    @Override
    @Transactional
    public UserCourseProgressResponse enrollCourseAfterPayment(String userId, String courseId) {
        // Verify course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        // Skip price check - this is called after payment is completed
        return performEnrollment(userId, courseId, course);
    }

    private UserCourseProgressResponse performEnrollment(String userId, String courseId, Course course) {
        // Check if already enrolled
        Optional<UserCourseProgress> existingProgress = userCourseProgressRepository.findByUserIdAndCourseId(userId,
                courseId);

        if (existingProgress.isPresent()) {
            throw new BadRequestException("User already enrolled in this course");
        }

        // Count total chapters
        long totalChapters = chapterRepository.countByCourseId(courseId);

        // Create progress record
        UserCourseProgress progress = new UserCourseProgress();
        progress.setUserId(userId);
        progress.setCourseId(courseId);
        progress.setCompletedChapters(0);
        progress.setTotalChapters((int) totalChapters);
        progress.setProgressPercentage(0.0);
        progress.setEnrolledAt(LocalDateTime.now());

        UserCourseProgress savedProgress = userCourseProgressRepository.save(progress);
        log.info("User {} enrolled in course {}", userId, courseId);

        return mapToResponse(savedProgress, course);
    }

    @Override
    @Transactional
    public void markChapterComplete(String userId, String chapterId) {
        // Verify chapter exists
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found with id: " + chapterId));

        String courseId = chapter.getCourseId();

        // Verify user is enrolled
        userCourseProgressRepository
                .findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new BadRequestException("User not enrolled in this course"));

        // Check if chapter already completed
        Optional<UserChapterProgress> existingChapterProgress = userChapterProgressRepository
                .findByUserIdAndChapterId(userId, chapterId);

        if (existingChapterProgress.isPresent() && existingChapterProgress.get().getIsCompleted()) {
            log.info("Chapter {} already completed by user {}", chapterId, userId);
            return; // Already completed
        }

        // Mark chapter as completed
        UserChapterProgress chapterProgress = existingChapterProgress.orElse(new UserChapterProgress());
        chapterProgress.setUserId(userId);
        chapterProgress.setChapterId(chapterId);
        chapterProgress.setIsCompleted(true);
        chapterProgress.setCompletedAt(LocalDateTime.now());

        userChapterProgressRepository.save(chapterProgress);
        log.info("Chapter {} marked as completed by user {}", chapterId, userId);

        // Recalculate course progress
        recalculateCourseProgress(userId, courseId);
    }

    @Override
    public UserCourseProgressResponse getCourseProgress(String userId, String courseId) {
        // Verify course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        UserCourseProgress progress = userCourseProgressRepository
                .findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not enrolled in course with id: " + courseId));

        return mapToResponse(progress, course);
    }

    @Override
    public List<UserCourseProgressResponse> getAllUserProgress(String userId) {
        List<UserCourseProgress> progressList = userCourseProgressRepository.findByUserId(userId);

        return progressList.stream()
                .map(progress -> {
                    Course course = courseRepository.findById(progress.getCourseId()).orElse(null);
                    return course != null ? mapToResponse(progress, course) : null;
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
    }

    @Override
    public StudentStatsResponse getStudentStats(String userId) {
        // Get all course progress
        List<UserCourseProgress> allProgress = userCourseProgressRepository.findByUserId(userId);

        int totalEnrolled = allProgress.size();
        int completedCourses = (int) allProgress.stream()
                .filter(p -> p.getProgressPercentage() == 100)
                .count();
        int inProgress = totalEnrolled - completedCourses;

        // Get total chapters completed
        List<UserChapterProgress> chapterProgress = userChapterProgressRepository.findByUserId(userId);
        int totalChaptersCompleted = (int) chapterProgress.stream()
                .filter(UserChapterProgress::getIsCompleted)
                .count();

        // Calculate total time spent
        int totalTimeSpent = chapterProgress.stream()
                .mapToInt(cp -> cp.getTimeSpentMinutes() != null ? cp.getTimeSpentMinutes() : 0)
                .sum();

        // Calculate completion rate
        int completionRate = totalEnrolled == 0 ? 0 : (int) Math.round((double) completedCourses / totalEnrolled * 100);

        return StudentStatsResponse.builder()
                .totalCoursesEnrolled(totalEnrolled)
                .completedCourses(completedCourses)
                .inProgressCourses(inProgress)
                .totalChaptersCompleted(totalChaptersCompleted)
                .totalTimeSpentMinutes(totalTimeSpent)
                .completionRate(completionRate)
                .build();
    }

    @Override
    @Transactional
    public void recalculateCourseProgress(String userId, String courseId) {
        UserCourseProgress courseProgress = userCourseProgressRepository
                .findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not enrolled in course with id: " + courseId));

        // Get all chapters for this course
        List<Chapter> allChapters = chapterRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
        int totalChapters = allChapters.size();

        // Count completed chapters
        long completedCount = 0;
        for (Chapter chapter : allChapters) {
            Optional<UserChapterProgress> chapterProgress = userChapterProgressRepository
                    .findByUserIdAndChapterId(userId, chapter.getId());

            if (chapterProgress.isPresent() && chapterProgress.get().getIsCompleted()) {
                completedCount++;
            }
        }

        // Calculate percentage
        double percentage = totalChapters == 0 ? 0.0
                : Math.round((double) completedCount / totalChapters * 100.0 * 100.0) / 100.0;

        // Update progress
        courseProgress.setCompletedChapters((int) completedCount);
        courseProgress.setTotalChapters(totalChapters);
        courseProgress.setProgressPercentage(percentage);
        courseProgress.setLastAccessedAt(LocalDateTime.now());

        // Set completion date if 100%
        if (percentage >= 100.0 && courseProgress.getCompletedAt() == null) {
            courseProgress.setCompletedAt(LocalDateTime.now());
            log.info("User {} completed course {}", userId, courseId);
        }

        userCourseProgressRepository.save(courseProgress);
        log.info("Course progress updated for user {} on course {}: {}%",
                userId, courseId, percentage);
    }

    private UserCourseProgressResponse mapToResponse(UserCourseProgress progress, Course course) {
        return UserCourseProgressResponse.builder()
                .id(progress.getId())
                .userId(progress.getUserId())
                .courseId(progress.getCourseId())
                .courseTitle(course.getTitle())
                .completedChapters(progress.getCompletedChapters())
                .totalChapters(progress.getTotalChapters())
                .progressPercentage(progress.getProgressPercentage())
                .enrolledAt(progress.getEnrolledAt())
                .completedAt(progress.getCompletedAt())
                .lastAccessedAt(progress.getLastAccessedAt())
                .build();
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        // Get all users with role USER
        List<com.tobi.MusicLearn_Studio_Backend.modules.auth.entity.User> users = userRepository.findAll().stream()
                .filter(user -> "USER".equals(user.getRole()))
                .collect(Collectors.toList());

        return users.stream().map(user -> {
            // Count enrolled courses
            int enrolledCount = userCourseProgressRepository.findByUserId(user.getId()).size();

            return StudentResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .createdAt(user.getCreatedAt())
                    .enrolledCourses(enrolledCount)
                    .isLocked(user.getIsLocked())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public AdminProgressStatsResponse getAdminProgressStats() {
        // Total students (users with role USER)
        long totalStudents = userRepository.findAll().stream()
                .filter(user -> "USER".equals(user.getRole()))
                .count();

        // Completed lessons (chapters)
        long completedLessons = userChapterProgressRepository.findAll().stream()
                .filter(UserChapterProgress::getIsCompleted)
                .count();

        // Avg hours (placeholder as we don't track time yet)
        double avgHours = 0.0;

        // Avg quiz score (placeholder)
        double avgQuizScore = 0.0;

        return AdminProgressStatsResponse.builder()
                .totalStudents(totalStudents)
                .completedLessons(completedLessons)
                .avgHours(avgHours)
                .avgQuizScore(avgQuizScore)
                .build();
    }

    @Override
    public List<CourseProgressStatsResponse> getCourseProgressStats() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream().map(course -> {
            List<UserCourseProgress> progressList = userCourseProgressRepository.findByCourseId(course.getId());
            long enrolledStudents = progressList.size();

            double avgCompletion = 0.0;
            if (enrolledStudents > 0) {
                double totalPercentage = progressList.stream()
                        .mapToDouble(UserCourseProgress::getProgressPercentage)
                        .sum();
                avgCompletion = Math.round((totalPercentage / enrolledStudents) * 100.0) / 100.0;
            }

            return CourseProgressStatsResponse.builder()
                    .id(course.getId())
                    .name(course.getTitle())
                    .enrolledStudents(enrolledStudents)
                    .completionRate(avgCompletion)
                    .build();
        }).collect(Collectors.toList());
    }
}
