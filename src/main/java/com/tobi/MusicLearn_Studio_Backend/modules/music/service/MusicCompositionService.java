package com.tobi.MusicLearn_Studio_Backend.modules.music.service;

import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.CreateCompositionRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request.UpdateCompositionRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response.CompositionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MusicCompositionService {

    /**
     * Create a new composition (initially as DRAFT)
     */
    CompositionResponse createComposition(CreateCompositionRequest request, String userId);

    /**
     * Update an existing composition
     */
    CompositionResponse updateComposition(String compositionId, UpdateCompositionRequest request, String userId);

    /**
     * Get composition by ID
     */
    CompositionResponse getCompositionById(String compositionId, String userId);

    /**
     * Get all compositions for a user
     */
    List<CompositionResponse> getUserCompositions(String userId);

    /**
     * Get drafts for a user
     */
    List<CompositionResponse> getUserDrafts(String userId);

    /**
     * Get published compositions for a user
     */
    List<CompositionResponse> getUserPublishedCompositions(String userId);

    /**
     * Get all public published compositions
     */
    List<CompositionResponse> getPublicCompositions();

    /**
     * Publish a composition (convert from DRAFT to PUBLISHED)
     * Requires audio file to be uploaded
     */
    CompositionResponse publishComposition(String compositionId, MultipartFile audioFile, String userId)
            throws IOException;

    /**
     * Delete a composition
     */
    void deleteComposition(String compositionId, String userId);

    /**
     * Duplicate a composition as a new draft
     */
    CompositionResponse duplicateComposition(String compositionId, String userId);
}
