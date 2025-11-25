package com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {

    private String id;
    private String title;
    private String slug;
    private String description;
    private String thumbnailUrl;
    private String level;
    private Boolean isPublished;
    private Integer orderIndex;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long totalChapters;
    private Double price; // Price of the course (0.0 = free)
}
