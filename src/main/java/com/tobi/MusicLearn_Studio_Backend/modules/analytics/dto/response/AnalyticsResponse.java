package com.tobi.MusicLearn_Studio_Backend.modules.analytics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private AnalyticsStats stats;
    private List<RevenueDataPoint> revenueOverTime;
    private List<PopularCourseData> popularCourses;
    private List<StudentGrowthData> studentGrowth;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnalyticsStats {
        private Double totalRevenue;
        private Long totalStudents;
        private Long totalCourses;
        private Long activeEnrollments;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueDataPoint {
        private String month;
        private Double amount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopularCourseData {
        private String courseName;
        private Long studentCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentGrowthData {
        private String month;
        private Long count;
    }
}
