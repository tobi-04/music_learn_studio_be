package com.tobi.MusicLearn_Studio_Backend.modules.learning.entity;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "courses")
public class Course extends BaseEntity {

    @Indexed(unique = true)
    private String title;

    @Indexed(unique = true)
    private String slug;

    private String description;

    private String thumbnailUrl;

    private String level; // beginner, intermediate, advanced

    private Boolean isPublished = false;

    private Integer orderIndex = 0;

    private Double price = 0.0; // Price of the course (0.0 = free)

    private Long totalStudents = 0L; // Total number of enrolled students
}
