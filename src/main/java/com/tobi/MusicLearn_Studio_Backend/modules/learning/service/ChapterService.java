package com.tobi.MusicLearn_Studio_Backend.modules.learning.service;

import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.CreateChapterRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.UpdateChapterRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.ChapterResponse;

import java.util.List;

public interface ChapterService {

    /**
     * Create a new chapter
     */
    ChapterResponse createChapter(String courseId, CreateChapterRequest request, String userId);

    /**
     * Update an existing chapter
     */
    ChapterResponse updateChapter(String id, UpdateChapterRequest request);

    /**
     * Delete a chapter
     */
    void deleteChapter(String id);

    /**
     * Get chapter by ID
     */
    ChapterResponse getChapterById(String id);

    /**
     * Get chapter by slug
     */
    ChapterResponse getChapterBySlug(String slug);

    /**
     * Get all chapters for a course
     */
    List<ChapterResponse> getChaptersByCourse(String courseId);

    /**
     * Get published chapters for a course
     */
    List<ChapterResponse> getPublishedChaptersByCourse(String courseId);

    /**
     * Toggle chapter publish status
     */
    ChapterResponse togglePublish(String id);
}
