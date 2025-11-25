package com.tobi.MusicLearn_Studio_Backend.modules.learning.service;

import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.CreateCourseRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request.UpdateCourseRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.CourseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response.CourseWithDetailsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CourseService {

    CourseResponse createCourse(CreateCourseRequest request, MultipartFile thumbnailFile, String userId)
            throws IOException;

    CourseResponse updateCourse(String id, UpdateCourseRequest request, MultipartFile thumbnailFile) throws IOException;

    void deleteCourse(String id);

    CourseResponse getCourseById(String id);

    CourseResponse getCourseBySlug(String slug);

    CourseWithDetailsResponse getCourseWithChapters(String id);

    CourseWithDetailsResponse getCourseWithChaptersBySlug(String slug);

    Page<CourseResponse> getAllCourses(Pageable pageable);

    Page<CourseResponse> getCoursesByLevel(String level, Pageable pageable);

    Page<CourseResponse> getCoursesByPublished(Boolean isPublished, Pageable pageable);

    Page<CourseResponse> searchCourses(String keyword, Pageable pageable);

    CourseResponse togglePublish(String id);
}
