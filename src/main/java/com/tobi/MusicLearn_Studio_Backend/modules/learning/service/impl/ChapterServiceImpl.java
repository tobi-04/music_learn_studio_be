package com.tobi.MusicLearn_Studio_Backend.modules.learning.service.impl;

import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import com.tobi.MusicLearn_Studio_Backend.common.service.MarkdownService;
import com.tobi.MusicLearn_Studio_Backend.common.service.SlugService;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.CreateChapterRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.UpdateChapterRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.ChapterResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.Chapter;

import com.tobi.MusicLearn_Studio_Backend.modules.learning.repository.ChapterRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.repository.CourseRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final SlugService slugService;
    private final MarkdownService markdownService;

    @Override
    @Transactional
    public ChapterResponse createChapter(String courseId, CreateChapterRequest request, String userId) {
        // Verify course exists
        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        // Generate unique slug
        String baseSlug = slugService.generateSlug(request.getTitle());
        String uniqueSlug = slugService.generateUniqueSlug(
                baseSlug,
                chapterRepository::existsBySlug);

        // Sanitize markdown content
        String sanitizedContent = markdownService.sanitize(request.getContentMarkdown());

        // Create chapter
        Chapter chapter = new Chapter();
        chapter.setCourseId(courseId);
        chapter.setTitle(request.getTitle());
        chapter.setSlug(uniqueSlug);
        chapter.setContentMarkdown(sanitizedContent);
        chapter.setVideoUrl(request.getVideoUrl());
        chapter.setDurationMinutes(request.getDurationMinutes());
        chapter.setOrderIndex(request.getOrderIndex() != null ? request.getOrderIndex() : 0);
        chapter.setIsPublished(false); // Always false initially

        Chapter savedChapter = chapterRepository.save(chapter);
        return mapToResponse(savedChapter);
    }

    @Override
    @Transactional
    public ChapterResponse updateChapter(String id, UpdateChapterRequest request) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found with id: " + id));

        // Update title and slug if title changed
        if (request.getTitle() != null && !request.getTitle().equals(chapter.getTitle())) {
            chapter.setTitle(request.getTitle());

            String baseSlug = slugService.generateSlug(request.getTitle());
            String uniqueSlug = slugService.generateUniqueSlug(
                    baseSlug,
                    slug -> chapterRepository.existsBySlug(slug) && !slug.equals(chapter.getSlug()));
            chapter.setSlug(uniqueSlug);
        }

        // Update content markdown with sanitization
        if (request.getContentMarkdown() != null) {
            String sanitizedContent = markdownService.sanitize(request.getContentMarkdown());
            chapter.setContentMarkdown(sanitizedContent);
        }

        // Update other fields if provided
        if (request.getVideoUrl() != null) {
            chapter.setVideoUrl(request.getVideoUrl());
        }

        if (request.getDurationMinutes() != null) {
            chapter.setDurationMinutes(request.getDurationMinutes());
        }

        if (request.getOrderIndex() != null) {
            chapter.setOrderIndex(request.getOrderIndex());
        }

        if (request.getIsPublished() != null) {
            chapter.setIsPublished(request.getIsPublished());
        }

        Chapter updatedChapter = chapterRepository.save(chapter);
        return mapToResponse(updatedChapter);
    }

    @Override
    @Transactional
    public void deleteChapter(String id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found with id: " + id));

        chapterRepository.delete(chapter);
    }

    @Override
    public ChapterResponse getChapterById(String id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found with id: " + id));

        return mapToResponse(chapter);
    }

    @Override
    public ChapterResponse getChapterBySlug(String slug) {
        Chapter chapter = chapterRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found with slug: " + slug));

        return mapToResponse(chapter);
    }

    @Override
    public List<ChapterResponse> getChaptersByCourse(String courseId) {
        // Verify course exists
        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        List<Chapter> chapters = chapterRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
        return chapters.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChapterResponse> getPublishedChaptersByCourse(String courseId) {
        // Verify course exists
        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        List<Chapter> chapters = chapterRepository.findByCourseIdAndIsPublishedOrderByOrderIndexAsc(courseId, true);
        return chapters.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChapterResponse togglePublish(String id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found with id: " + id));

        chapter.setIsPublished(!chapter.getIsPublished());
        Chapter updatedChapter = chapterRepository.save(chapter);

        return mapToResponse(updatedChapter);
    }

    private ChapterResponse mapToResponse(Chapter chapter) {
        return ChapterResponse.builder()
                .id(chapter.getId())
                .courseId(chapter.getCourseId())
                .title(chapter.getTitle())
                .slug(chapter.getSlug())
                .contentMarkdown(chapter.getContentMarkdown())
                .videoUrl(chapter.getVideoUrl())
                .durationMinutes(chapter.getDurationMinutes())
                .orderIndex(chapter.getOrderIndex())
                .isPublished(chapter.getIsPublished())
                .createdAt(chapter.getCreatedAt())
                .updatedAt(chapter.getUpdatedAt())
                .build();
    }
}
