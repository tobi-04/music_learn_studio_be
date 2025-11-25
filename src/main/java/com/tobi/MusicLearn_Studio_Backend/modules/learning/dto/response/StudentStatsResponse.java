package com.tobi.MusicLearn_Studio_Backend.modules.learning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentStatsResponse {

    private Integer totalCoursesEnrolled;
    private Integer completedCourses;
    private Integer inProgressCourses;
    private Integer totalChaptersCompleted;
    private Integer totalTimeSpentMinutes;
    private Integer completionRate; // Percentage
}
