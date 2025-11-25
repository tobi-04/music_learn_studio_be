package com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequest {

    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    private String thumbnailUrl;

    private String level;

    private Integer orderIndex;

    private Boolean isPublished;

    private Double price; // Price of the course (0.0 = free)
}
