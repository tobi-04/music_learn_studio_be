package com.tobi.MusicLearn_Studio_Backend.modules.learning.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.CreateChapterRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.UpdateChapterRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.ChapterResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Chapter Management", description = "APIs for managing chapters")
public class ChapterController {

    private final ChapterService chapterService;

    @PostMapping("/courses/{courseId}/chapters")
    @Operation(summary = "Create a new chapter")
    public ResponseEntity<BaseResponse<ChapterResponse>> createChapter(
            @PathVariable String courseId,
            @Valid @RequestBody CreateChapterRequest request,
            @RequestHeader("X-User-Id") String userId) {

        ChapterResponse response = chapterService.createChapter(courseId, request, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<ChapterResponse>builder()
                        .success(true)
                        .message("Chapter created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/courses/{courseId}/chapters")
    @Operation(summary = "Get all chapters for a course")
    public ResponseEntity<BaseResponse<List<ChapterResponse>>> getChaptersByCourse(
            @PathVariable String courseId,
            @RequestParam(required = false, defaultValue = "false") boolean publishedOnly) {

        List<ChapterResponse> chapters = publishedOnly
                ? chapterService.getPublishedChaptersByCourse(courseId)
                : chapterService.getChaptersByCourse(courseId);

        return ResponseEntity.ok(BaseResponse.<List<ChapterResponse>>builder()
                .success(true)
                .message("Chapters retrieved successfully")
                .data(chapters)
                .build());
    }

    @GetMapping("/chapters/{id}")
    @Operation(summary = "Get chapter by ID")
    public ResponseEntity<BaseResponse<ChapterResponse>> getChapterById(@PathVariable String id) {
        ChapterResponse response = chapterService.getChapterById(id);

        return ResponseEntity.ok(BaseResponse.<ChapterResponse>builder()
                .success(true)
                .message("Chapter retrieved successfully")
                .data(response)
                .build());
    }

    @GetMapping("/chapters/slug/{slug}")
    @Operation(summary = "Get chapter by slug")
    public ResponseEntity<BaseResponse<ChapterResponse>> getChapterBySlug(@PathVariable String slug) {
        ChapterResponse response = chapterService.getChapterBySlug(slug);

        return ResponseEntity.ok(BaseResponse.<ChapterResponse>builder()
                .success(true)
                .message("Chapter retrieved successfully")
                .data(response)
                .build());
    }

    @PutMapping("/chapters/{id}")
    @Operation(summary = "Update chapter")
    public ResponseEntity<BaseResponse<ChapterResponse>> updateChapter(
            @PathVariable String id,
            @Valid @RequestBody UpdateChapterRequest request) {

        ChapterResponse response = chapterService.updateChapter(id, request);

        return ResponseEntity.ok(BaseResponse.<ChapterResponse>builder()
                .success(true)
                .message("Chapter updated successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/chapters/{id}")
    @Operation(summary = "Delete chapter")
    public ResponseEntity<BaseResponse<Void>> deleteChapter(@PathVariable String id) {
        chapterService.deleteChapter(id);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .success(true)
                .message("Chapter deleted successfully")
                .build());
    }

    @PatchMapping("/chapters/{id}/publish")
    @Operation(summary = "Toggle chapter publish status")
    public ResponseEntity<BaseResponse<ChapterResponse>> togglePublish(@PathVariable String id) {
        ChapterResponse response = chapterService.togglePublish(id);

        return ResponseEntity.ok(BaseResponse.<ChapterResponse>builder()
                .success(true)
                .message("Chapter publish status updated successfully")
                .data(response)
                .build());
    }
}
