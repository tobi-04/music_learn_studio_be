package com.tobi.MusicLearn_Studio_Backend.modules.enrollment.entity;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "enrollments")
@CompoundIndex(name = "user_course_idx", def = "{'userId': 1, 'courseId': 1}", unique = true)
public class Enrollment extends BaseEntity {

    @Indexed
    private String userId; // User who enrolled

    @Indexed
    private String courseId; // Course they enrolled in

    @Indexed
    private LocalDateTime enrolledAt; // When they enrolled

    private LocalDateTime completedAt; // When they completed (null if not completed)

    private Integer progress; // Progress percentage (0-100)

    private Boolean isActive; // Whether enrollment is active
}
