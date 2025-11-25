package com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterResponse {

    private String id;
    private String courseId;
    private String title;
    private String slug;
    private String description;
    private String contentMarkdown;
    private String videoUrl;
    private Integer durationMinutes;
    private Boolean isPublished;
    private Integer orderIndex;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
