package com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private String id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private Integer enrolledCourses;
    private Boolean isLocked;
}
