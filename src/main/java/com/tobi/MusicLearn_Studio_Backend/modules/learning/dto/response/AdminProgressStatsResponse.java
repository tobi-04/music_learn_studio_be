package com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProgressStatsResponse {
    private Long totalStudents;
    private Long completedLessons;
    private Double avgHours;
    private Double avgQuizScore;
}
