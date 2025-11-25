package com.tobi.MusicLearn_Studio_Backend.modules.learning.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.StudentStatsResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.UserCourseProgressResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student/progress")
@RequiredArgsConstructor
@Tag(name = "Progress Tracking", description = "APIs for tracking student progress")
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping("/enroll/{courseId}")
    @Operation(summary = "Enroll in a course")
    public ResponseEntity<BaseResponse<UserCourseProgressResponse>> enrollCourse(
            @PathVariable String courseId,
            @RequestHeader("X-User-Id") String userId) {

        UserCourseProgressResponse response = progressService.enrollCourse(userId, courseId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<UserCourseProgressResponse>builder()
                        .success(true)
                        .message("Enrolled in course successfully")
                        .data(response)
                        .build());
    }

    @PostMapping("/chapters/{chapterId}/complete")
    @Operation(summary = "Mark chapter as completed")
    public ResponseEntity<BaseResponse<Void>> markChapterComplete(
            @PathVariable String chapterId,
            @RequestHeader("X-User-Id") String userId) {

        progressService.markChapterComplete(userId, chapterId);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .success(true)
                .message("Chapter marked as completed")
                .build());
    }

    @GetMapping("/courses/{courseId}")
    @Operation(summary = "Get progress for a specific course")
    public ResponseEntity<BaseResponse<UserCourseProgressResponse>> getCourseProgress(
            @PathVariable String courseId,
            @RequestHeader("X-User-Id") String userId) {

        UserCourseProgressResponse response = progressService.getCourseProgress(userId, courseId);

        return ResponseEntity.ok(BaseResponse.<UserCourseProgressResponse>builder()
                .success(true)
                .message("Course progress retrieved successfully")
                .data(response)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all course progress for current user")
    public ResponseEntity<BaseResponse<List<UserCourseProgressResponse>>> getAllUserProgress(
            @RequestHeader("X-User-Id") String userId) {

        List<UserCourseProgressResponse> progressList = progressService.getAllUserProgress(userId);

        return ResponseEntity.ok(BaseResponse.<List<UserCourseProgressResponse>>builder()
                .success(true)
                .message("Progress retrieved successfully")
                .data(progressList)
                .build());
    }

    @GetMapping("/stats")
    @Operation(summary = "Get student statistics")
    public ResponseEntity<BaseResponse<StudentStatsResponse>> getStudentStats(
            @RequestHeader("X-User-Id") String userId) {

        StudentStatsResponse stats = progressService.getStudentStats(userId);

        return ResponseEntity.ok(BaseResponse.<StudentStatsResponse>builder()
                .success(true)
                .message("Student stats retrieved successfully")
                .data(stats)
                .build());
    }
}
