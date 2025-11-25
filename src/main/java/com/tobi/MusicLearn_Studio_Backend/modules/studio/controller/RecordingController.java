package com.tobi.MusicLearn_Studio_Backend.modules.studio.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.request.CreateRecordingRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.response.RecordingResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.service.RecordingService;
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
@RequestMapping("/api/v1/studio/recordings")
@RequiredArgsConstructor
@Tag(name = "Studio Recordings", description = "APIs for audio recording management")
public class RecordingController {

    private final RecordingService recordingService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a new audio recording")
    public ResponseEntity<BaseResponse<RecordingResponse>> uploadRecording(
            @RequestPart("file") MultipartFile file,
            @Valid @RequestPart("data") CreateRecordingRequest request,
            @RequestHeader("X-User-Id") String userId) throws IOException {

        RecordingResponse response = recordingService.uploadRecording(file, request, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<RecordingResponse>builder()
                        .success(true)
                        .message("Recording uploaded successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get all recordings for a project")
    public ResponseEntity<BaseResponse<List<RecordingResponse>>> getRecordingsByProject(
            @PathVariable String projectId,
            @RequestHeader("X-User-Id") String userId) {

        List<RecordingResponse> recordings = recordingService.getRecordingsByProject(projectId, userId);

        return ResponseEntity.ok(BaseResponse.<List<RecordingResponse>>builder()
                .success(true)
                .message("Recordings retrieved successfully")
                .data(recordings)
                .build());
    }

    @GetMapping("/my-recordings")
    @Operation(summary = "Get all my recordings")
    public ResponseEntity<BaseResponse<List<RecordingResponse>>> getMyRecordings(
            @RequestHeader("X-User-Id") String userId) {

        List<RecordingResponse> recordings = recordingService.getMyRecordings(userId);

        return ResponseEntity.ok(BaseResponse.<List<RecordingResponse>>builder()
                .success(true)
                .message("Recordings retrieved successfully")
                .data(recordings)
                .build());
    }

    @GetMapping("/{recordingId}")
    @Operation(summary = "Get recording by ID")
    public ResponseEntity<BaseResponse<RecordingResponse>> getRecordingById(
            @PathVariable String recordingId,
            @RequestHeader("X-User-Id") String userId) {

        RecordingResponse recording = recordingService.getRecordingById(recordingId, userId);

        return ResponseEntity.ok(BaseResponse.<RecordingResponse>builder()
                .success(true)
                .message("Recording retrieved successfully")
                .data(recording)
                .build());
    }

    @DeleteMapping("/{recordingId}")
    @Operation(summary = "Delete a recording")
    public ResponseEntity<BaseResponse<Void>> deleteRecording(
            @PathVariable String recordingId,
            @RequestHeader("X-User-Id") String userId) {

        recordingService.deleteRecording(recordingId, userId);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .success(true)
                .message("Recording deleted successfully")
                .build());
    }
}
