package com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.progress;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "user_course_progress")
@CompoundIndex(name = "user_course_idx", def = "{'userId': 1, 'courseId': 1}", unique = true)
public class UserCourseProgress extends BaseEntity {

    private String userId;

    private String courseId;

    private Integer completedChapters = 0;

    private Integer totalChapters = 0;

    private Double progressPercentage = 0.0;

    private LocalDateTime enrolledAt;

    private LocalDateTime lastAccessedAt;

    private LocalDateTime completedAt;
}
