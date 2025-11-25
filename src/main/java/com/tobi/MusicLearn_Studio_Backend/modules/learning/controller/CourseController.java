package com.tobi.MusicLearn_Studio_Backend.modules.learning.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.common.dto.PageResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.CreateCourseRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.UpdateCourseRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.CourseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin/courses")
@RequiredArgsConstructor
@Tag(name = "Course Management", description = "APIs for managing courses")
public class CourseController {

    private final CourseService courseService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new course")
    public ResponseEntity<BaseResponse<CourseResponse>> createCourse(
            @Valid @RequestPart("data") CreateCourseRequest request,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnailFile,
            @RequestHeader("X-User-Id") String userId) throws IOException {

        CourseResponse response = courseService.createCourse(request, thumbnailFile, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<CourseResponse>builder()
                        .success(true)
                        .message("Course created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping
    @Operation(summary = "Get all courses with filters")
    public ResponseEntity<PageResponse<CourseResponse>> getAllCourses(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) Boolean published,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CourseResponse> coursePage;

        if (search != null && !search.trim().isEmpty()) {
            coursePage = courseService.searchCourses(search, pageable);
        } else if (level != null && published != null) {
            coursePage = courseService.getCoursesByPublished(published, pageable);
        } else if (level != null) {
            coursePage = courseService.getCoursesByLevel(level, pageable);
        } else if (published != null) {
            coursePage = courseService.getCoursesByPublished(published, pageable);
        } else {
            coursePage = courseService.getAllCourses(pageable);
        }

        return ResponseEntity.ok(PageResponse.<CourseResponse>builder()
                .content(coursePage.getContent())
                .pageNumber(coursePage.getNumber())
                .pageSize(coursePage.getSize())
                .totalElements(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .first(coursePage.isFirst())
                .last(coursePage.isLast())
                .empty(coursePage.isEmpty())
                .nextPage(coursePage.hasNext() ? coursePage.getNumber() + 1 : null)
                .previousPage(coursePage.hasPrevious() ? coursePage.getNumber() - 1 : null)
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<BaseResponse<CourseResponse>> getCourseById(@PathVariable String id) {
        CourseResponse response = courseService.getCourseById(id);

        return ResponseEntity.ok(BaseResponse.<CourseResponse>builder()
                .success(true)
                .message("Course retrieved successfully")
                .data(response)
                .build());
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get course by slug")
    public ResponseEntity<BaseResponse<CourseResponse>> getCourseBySlug(@PathVariable String slug) {
        CourseResponse response = courseService.getCourseBySlug(slug);

        return ResponseEntity.ok(BaseResponse.<CourseResponse>builder()
                .success(true)
                .message("Course retrieved successfully")
                .data(response)
                .build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update course")
    public ResponseEntity<BaseResponse<CourseResponse>> updateCourse(
            @PathVariable String id,
            @Valid @RequestPart("data") UpdateCourseRequest request,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnailFile) throws IOException {

        CourseResponse response = courseService.updateCourse(id, request, thumbnailFile);

        return ResponseEntity.ok(BaseResponse.<CourseResponse>builder()
                .success(true)
                .message("Course updated successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course")
    public ResponseEntity<BaseResponse<Void>> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .success(true)
                .message("Course deleted successfully")
                .build());
    }

    @PatchMapping("/{id}/publish")
    @Operation(summary = "Toggle course publish status")
    public ResponseEntity<BaseResponse<CourseResponse>> togglePublish(@PathVariable String id) {
        CourseResponse response = courseService.togglePublish(id);

        return ResponseEntity.ok(BaseResponse.<CourseResponse>builder()
                .success(true)
                .message("Course publish status updated successfully")
                .data(response)
                .build());
    }
}
