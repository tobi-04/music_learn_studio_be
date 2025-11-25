package com.tobi.MusicLearn_Studio_Backend.modules.learning.service.impl;

import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import com.tobi.MusicLearn_Studio_Backend.common.service.R2StorageService;
import com.tobi.MusicLearn_Studio_Backend.common.service.SlugService;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.CreateCourseRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.UpdateCourseRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.ChapterResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.CourseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.CourseWithDetailsResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.Chapter;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.Course;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.repository.ChapterRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.repository.CourseRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final SlugService slugService;
    private final R2StorageService r2StorageService;

    @Override
    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request, MultipartFile thumbnailFile, String userId)
            throws IOException {
        // Generate slug
        String baseSlug = slugService.generateSlug(request.getTitle());
        String uniqueSlug = slugService.generateUniqueSlug(baseSlug, courseRepository::existsBySlug);

        String thumbnailUrl = request.getThumbnailUrl();
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            thumbnailUrl = r2StorageService.uploadFile(thumbnailFile, "courses/thumbnails");
        }

        // Create course
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setSlug(uniqueSlug);
        course.setDescription(request.getDescription());
        course.setThumbnailUrl(thumbnailUrl);
        course.setLevel(request.getLevel());
        course.setOrderIndex(request.getOrderIndex());
        course.setPrice(request.getPrice());
        course.setIsPublished(false);
        course.setCreatedBy(userId);

        Course savedCourse = courseRepository.save(course);

        return mapToResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(String id, UpdateCourseRequest request, MultipartFile thumbnailFile)
            throws IOException {
        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        // Update fields if provided
        if (request.getTitle() != null && !request.getTitle().equals(course.getTitle())) {
            course.setTitle(request.getTitle());
            // Regenerate slug
            String baseSlug = slugService.generateSlug(request.getTitle());
            String uniqueSlug = slugService.generateUniqueSlug(
                    baseSlug,
                    slug -> !slug.equals(course.getSlug()) && courseRepository.existsBySlug(slug));
            course.setSlug(uniqueSlug);
        }

        if (request.getDescription() != null) {
            course.setDescription(request.getDescription());
        }

        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            String thumbnailUrl = r2StorageService.uploadFile(thumbnailFile, "courses/thumbnails");
            course.setThumbnailUrl(thumbnailUrl);
        } else if (request.getThumbnailUrl() != null) {
            course.setThumbnailUrl(request.getThumbnailUrl());
        }

        if (request.getLevel() != null) {
            course.setLevel(request.getLevel());
        }

        if (request.getOrderIndex() != null) {
            course.setOrderIndex(request.getOrderIndex());
        }

        if (request.getIsPublished() != null) {
            course.setIsPublished(request.getIsPublished());
        }

        if (request.getPrice() != null) {
            course.setPrice(request.getPrice());
        }

        Course updatedCourse = courseRepository.save(course);

        return mapToResponse(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(String id) {
        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        // Delete all chapters of this course
        chapterRepository.deleteByCourseId(id);

        // Delete course
        courseRepository.delete(course);
    }

    @Override
    public CourseResponse getCourseById(String id) {
        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        return mapToResponse(course);
    }

    @Override
    public CourseResponse getCourseBySlug(String slug) {
        Course course = courseRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with slug: " + slug));

        return mapToResponse(course);
    }

    @Override
    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);
        return courses.map(this::mapToResponse);
    }

    @Override
    public Page<CourseResponse> getCoursesByLevel(String level, Pageable pageable) {
        Page<Course> courses = courseRepository.findByLevel(level, pageable);
        return courses.map(this::mapToResponse);
    }

    @Override
    public Page<CourseResponse> getCoursesByPublished(Boolean isPublished, Pageable pageable) {
        Page<Course> courses = courseRepository.findByIsPublished(isPublished, pageable);
        return courses.map(this::mapToResponse);
    }

    @Override
    public Page<CourseResponse> searchCourses(String keyword, Pageable pageable) {
        Page<Course> courses = courseRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        return courses.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public CourseResponse togglePublish(String id) {
        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        course.setIsPublished(!course.getIsPublished());
        Course updatedCourse = courseRepository.save(course);

        return mapToResponse(updatedCourse);
    }

    @Override
    public CourseWithDetailsResponse getCourseWithChapters(String id) {
        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        return mapToDetailsResponse(course);
    }

    @Override
    public CourseWithDetailsResponse getCourseWithChaptersBySlug(String slug) {
        Course course = courseRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with slug: " + slug));

        return mapToDetailsResponse(course);
    }

    private CourseResponse mapToResponse(Course course) {
        Long totalChapters = chapterRepository.countByCourseId(course.getId());

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .slug(course.getSlug())
                .description(course.getDescription())
                .thumbnailUrl(course.getThumbnailUrl())
                .level(course.getLevel())
                .isPublished(course.getIsPublished())
                .orderIndex(course.getOrderIndex())
                .price(course.getPrice())
                .createdBy(course.getCreatedBy())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .totalChapters(totalChapters)
                .build();
    }

    private CourseWithDetailsResponse mapToDetailsResponse(Course course) {
        List<Chapter> chapters = chapterRepository.findByCourseIdAndIsPublishedOrderByOrderIndexAsc(
                course.getId(),
                true);

        List<ChapterResponse> chapterResponses = chapters.stream()
                .map(this::mapChapterToResponse)
                .toList();

        return CourseWithDetailsResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .slug(course.getSlug())
                .description(course.getDescription())
                .thumbnailUrl(course.getThumbnailUrl())
                .level(course.getLevel())
                .isPublished(course.getIsPublished())
                .orderIndex(course.getOrderIndex())
                .price(course.getPrice())
                .totalStudents(course.getTotalStudents())
                .createdBy(course.getCreatedBy())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .totalChapters((long) chapters.size())
                .chapters(chapterResponses)
                .build();
    }

    private ChapterResponse mapChapterToResponse(Chapter chapter) {
        return ChapterResponse.builder()
                .id(chapter.getId())
                .courseId(chapter.getCourseId())
                .title(chapter.getTitle())
                .slug(chapter.getSlug())
                .description(chapter.getDescription())
                .contentMarkdown(chapter.getContentMarkdown())
                .videoUrl(chapter.getVideoUrl())
                .durationMinutes(chapter.getDurationMinutes())
                .isPublished(chapter.getIsPublished())
                .orderIndex(chapter.getOrderIndex())
                .createdAt(chapter.getCreatedAt())
                .updatedAt(chapter.getUpdatedAt())
                .build();
    }
}
