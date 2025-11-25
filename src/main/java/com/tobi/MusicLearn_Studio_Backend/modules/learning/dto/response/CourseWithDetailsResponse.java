package com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseWithDetailsResponse {

    // Basic course info (from CourseResponse)
    private String id;
    private String title;
    private String slug;
    private String description;
    private String thumbnailUrl;
    private String level;
    private Boolean isPublished;
    private Integer orderIndex;
    private Long totalChapters;
    private Double price;
    private Long totalStudents;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional: List of chapters
    private List<ChapterResponse> chapters;
}
