package com.tobi.MusicLearn_Studio_Backend.modules.music.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.CreateCompositionRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.UpdateCompositionRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response.CompositionResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.music.service.MusicCompositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/music/compositions")
@RequiredArgsConstructor
@Tag(name = "Music Compositions", description = "APIs for music composition management")
public class MusicCompositionController {

    private final MusicCompositionService compositionService;

    @PostMapping
    @Operation(summary = "Create a new composition (as draft)")
    public ResponseEntity<BaseResponse<CompositionResponse>> createComposition(
            @Valid @RequestBody CreateCompositionRequest request,
            @RequestHeader("X-User-Id") String userId) {

        CompositionResponse response = compositionService.createComposition(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<CompositionResponse>builder()
                        .success(true)
                        .message("Composition created successfully")
                        .data(response)
                        .build());
    }

    @PutMapping("/{compositionId}")
    @Operation(summary = "Update a composition")
    public ResponseEntity<BaseResponse<CompositionResponse>> updateComposition(
            @PathVariable String compositionId,
            @Valid @RequestBody UpdateCompositionRequest request,
            @RequestHeader("X-User-Id") String userId) {

        CompositionResponse response = compositionService.updateComposition(compositionId, request, userId);

        return ResponseEntity.ok(BaseResponse.<CompositionResponse>builder()
                .success(true)
                .message("Composition updated successfully")
                .data(response)
                .build());
    }

    @GetMapping("/{compositionId}")
    @Operation(summary = "Get composition by ID")
    public ResponseEntity<BaseResponse<CompositionResponse>> getCompositionById(
            @PathVariable String compositionId,
            @RequestHeader("X-User-Id") String userId) {

        CompositionResponse response = compositionService.getCompositionById(compositionId, userId);

        return ResponseEntity.ok(BaseResponse.<CompositionResponse>builder()
                .success(true)
                .message("Composition retrieved successfully")
                .data(response)
                .build());
    }

    @GetMapping("/my-compositions")
    @Operation(summary = "Get all compositions for current user")
    public ResponseEntity<BaseResponse<List<CompositionResponse>>> getMyCompositions(
            @RequestHeader("X-User-Id") String userId) {

        List<CompositionResponse> compositions = compositionService.getUserCompositions(userId);

        return ResponseEntity.ok(BaseResponse.<List<CompositionResponse>>builder()
                .success(true)
                .message("Compositions retrieved successfully")
                .data(compositions)
                .build());
    }

    @GetMapping("/my-drafts")
    @Operation(summary = "Get draft compositions for current user")
    public ResponseEntity<BaseResponse<List<CompositionResponse>>> getMyDrafts(
            @RequestHeader("X-User-Id") String userId) {

        List<CompositionResponse> drafts = compositionService.getUserDrafts(userId);

        return ResponseEntity.ok(BaseResponse.<List<CompositionResponse>>builder()
                .success(true)
                .message("Drafts retrieved successfully")
                .data(drafts)
                .build());
    }

    @GetMapping("/my-published")
    @Operation(summary = "Get published compositions for current user")
    public ResponseEntity<BaseResponse<List<CompositionResponse>>> getMyPublished(
            @RequestHeader("X-User-Id") String userId) {

        List<CompositionResponse> published = compositionService.getUserPublishedCompositions(userId);

        return ResponseEntity.ok(BaseResponse.<List<CompositionResponse>>builder()
                .success(true)
                .message("Published compositions retrieved successfully")
                .data(published)
                .build());
    }

    @GetMapping("/public")
    @Operation(summary = "Get all public published compositions")
    public ResponseEntity<BaseResponse<List<CompositionResponse>>> getPublicCompositions() {

        List<CompositionResponse> compositions = compositionService.getPublicCompositions();

        return ResponseEntity.ok(BaseResponse.<List<CompositionResponse>>builder()
                .success(true)
                .message("Public compositions retrieved successfully")
                .data(compositions)
                .build());
    }

    @PostMapping(value = "/{compositionId}/publish", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Publish a composition with audio file")
    public ResponseEntity<BaseResponse<CompositionResponse>> publishComposition(
            @PathVariable String compositionId,
            @RequestPart("audioFile") MultipartFile audioFile,
            @RequestHeader("X-User-Id") String userId) throws IOException {

        CompositionResponse response = compositionService.publishComposition(compositionId, audioFile, userId);

        return ResponseEntity.ok(BaseResponse.<CompositionResponse>builder()
                .success(true)
                .message("Composition published successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/{compositionId}")
    @Operation(summary = "Delete a composition")
    public ResponseEntity<BaseResponse<Void>> deleteComposition(
            @PathVariable String compositionId,
            @RequestHeader("X-User-Id") String userId) {

        compositionService.deleteComposition(compositionId, userId);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .success(true)
                .message("Composition deleted successfully")
                .build());
    }

    @PostMapping("/{compositionId}/duplicate")
    @Operation(summary = "Duplicate a composition as a new draft")
    public ResponseEntity<BaseResponse<CompositionResponse>> duplicateComposition(
            @PathVariable String compositionId,
            @RequestHeader("X-User-Id") String userId) {

        CompositionResponse response = compositionService.duplicateComposition(compositionId, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<CompositionResponse>builder()
                        .success(true)
                        .message("Composition duplicated successfully")
                        .data(response)
                        .build());
    }
}
