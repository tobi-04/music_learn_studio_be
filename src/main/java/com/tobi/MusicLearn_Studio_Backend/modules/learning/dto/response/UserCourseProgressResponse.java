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
public class UserCourseProgressResponse {

    private String id;
    private String userId;
    private String courseId;
    private String courseTitle;
    private Integer completedChapters;
    private Integer totalChapters;
    private Double progressPercentage;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
    private LocalDateTime lastAccessedAt;
}
