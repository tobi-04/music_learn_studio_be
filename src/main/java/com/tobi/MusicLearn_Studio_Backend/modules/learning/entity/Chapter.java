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
@Document(collection = "chapters")
public class Chapter extends BaseEntity {

    @Indexed
    private String courseId; // Reference to Course

    private String title;

    @Indexed
    private String slug;

    private String description;

    private String contentMarkdown; // Nội dung lý thuyết

    private String videoUrl; // Link YouTube/Vimeo

    private Integer durationMinutes;

    private Boolean isPublished = false;

    private Integer orderIndex = 0;
}
