package com.tobi.MusicLearn_Studio_Backend.modules.studio.service;

import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.request.CreateRecordingRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.response.RecordingResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RecordingService {

    /**
     * Upload a new recording with audio file
     */
    RecordingResponse uploadRecording(MultipartFile file, CreateRecordingRequest request, String userId)
            throws IOException;

    /**
     * Get all recordings for a project
     */
    List<RecordingResponse> getRecordingsByProject(String projectId, String userId);

    /**
     * Get all recordings by user
     */
    List<RecordingResponse> getMyRecordings(String userId);

    /**
     * Get recording by ID
     */
    RecordingResponse getRecordingById(String recordingId, String userId);

    /**
     * Delete a recording
     */
    void deleteRecording(String recordingId, String userId);
}
