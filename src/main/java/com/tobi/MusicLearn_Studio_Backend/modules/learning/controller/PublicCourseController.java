package com.tobi.MusicLearn_Studio_Backend.modules.learning.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.common.dto.PageResponse;
import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.CourseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.CourseWithDetailsResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/courses")
@RequiredArgsConstructor
@Tag(name = "Public Courses", description = "Public APIs for browsing courses (no authentication required)")
public class PublicCourseController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all published courses (public endpoint)")
    public ResponseEntity<PageResponse<CourseResponse>> getPublicCourses(
            @RequestParam(required = false) String level,
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

        // Always filter for published courses only
        if (search != null && !search.trim().isEmpty()) {
            coursePage = courseService.searchCourses(search, pageable);
        } else if (level != null && !level.trim().isEmpty()) {
            coursePage = courseService.getCoursesByLevel(level, pageable);
        } else {
            coursePage = courseService.getCoursesByPublished(true, pageable);
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
    @Operation(summary = "Get published course by ID with chapters (public endpoint)")
    public ResponseEntity<BaseResponse<CourseWithDetailsResponse>> getCourseById(@PathVariable String id) {
        CourseWithDetailsResponse course = courseService.getCourseWithChapters(id);

        // Ensure course is published
        if (!course.getIsPublished()) {
            throw new ResourceNotFoundException("Course not found or not published");
        }

        return ResponseEntity.ok(BaseResponse.<CourseWithDetailsResponse>builder()
                .success(true)
                .message("Course retrieved successfully")
                .data(course)
                .build());
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get published course by slug with chapters (public endpoint)")
    public ResponseEntity<BaseResponse<CourseWithDetailsResponse>> getCourseBySlug(@PathVariable String slug) {
        CourseWithDetailsResponse course = courseService.getCourseWithChaptersBySlug(slug);

        // Ensure course is published
        if (!course.getIsPublished()) {
            throw new ResourceNotFoundException("Course not found or not published");
        }

        return ResponseEntity.ok(BaseResponse.<CourseWithDetailsResponse>builder()
                .success(true)
                .message("Course retrieved successfully")
                .data(course)
                .build());
    }
}
