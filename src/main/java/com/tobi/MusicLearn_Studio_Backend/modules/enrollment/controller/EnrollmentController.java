package com.tobi.MusicLearn_Studio_Backend.modules.enrollment.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.enrollment.entity.Enrollment;
import com.tobi.MusicLearn_Studio_Backend.modules.enrollment.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/student/enrollments")
@RequiredArgsConstructor
@Tag(name = "Student Enrollments", description = "APIs for student course enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "Enroll in a course")
    public ResponseEntity<BaseResponse<Enrollment>> enrollInCourse(
            @RequestBody Map<String, String> request,
            @RequestHeader("X-User-Id") String userId) {

        String courseId = request.get("courseId");
        Enrollment enrollment = enrollmentService.createEnrollment(userId, courseId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<Enrollment>builder()
                        .success(true)
                        .message("Enrolled successfully")
                        .data(enrollment)
                        .build());
    }

    @GetMapping("/check/{courseId}")
    @Operation(summary = "Check if user is enrolled in a course")
    public ResponseEntity<BaseResponse<Map<String, Boolean>>> checkEnrollment(
            @PathVariable String courseId,
            @RequestHeader("X-User-Id") String userId) {

        boolean isEnrolled = enrollmentService.isUserEnrolled(userId, courseId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("enrolled", isEnrolled);

        return ResponseEntity.ok(BaseResponse.<Map<String, Boolean>>builder()
                .success(true)
                .message("Enrollment status retrieved")
                .data(response)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get user's enrollments")
    public ResponseEntity<BaseResponse<List<Enrollment>>> getUserEnrollments(
            @RequestHeader("X-User-Id") String userId) {

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUserId(userId);

        return ResponseEntity.ok(BaseResponse.<List<Enrollment>>builder()
                .success(true)
                .message("Enrollments retrieved successfully")
                .data(enrollments)
                .build());
    }

    @PatchMapping("/{enrollmentId}/progress")
    @Operation(summary = "Update enrollment progress")
    public ResponseEntity<BaseResponse<Void>> updateProgress(
            @PathVariable String enrollmentId,
            @RequestBody Map<String, Integer> request) {

        Integer progress = request.get("progress");
        enrollmentService.updateEnrollmentProgress(enrollmentId, progress);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .success(true)
                .message("Progress updated successfully")
                .build());
    }
}
