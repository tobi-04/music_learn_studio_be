package com.tobi.MusicLearn_Studio_Backend.modules.analytics.service.impl;

import com.tobi.MusicLearn_Studio_Backend.modules.analytics.dto.response.AnalyticsResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.analytics.service.AnalyticsService;
import com.tobi.MusicLearn_Studio_Backend.modules.enrollment.service.EnrollmentService;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.repository.CourseRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final PaymentService paymentService;
    private final EnrollmentService enrollmentService;
    private final CourseRepository courseRepository;

    @Override
    public AnalyticsResponse getAnalytics() {
        // Get real statistics
        Double totalRevenue = paymentService.getTotalRevenue();
        Long totalStudents = enrollmentService.getTotalStudents();
        Long totalCourses = courseRepository.count();
        Long activeEnrollments = enrollmentService.getActiveEnrollments();

        // Build stats
        AnalyticsResponse.AnalyticsStats stats = AnalyticsResponse.AnalyticsStats.builder()
                .totalRevenue(totalRevenue)
                .totalStudents(totalStudents)
                .totalCourses(totalCourses)
                .activeEnrollments(activeEnrollments)
                .build();

        // Get monthly revenue data (last 6 months)
        Map<String, Double> monthlyRevenueMap = paymentService.getMonthlyRevenue(6);
        List<AnalyticsResponse.RevenueDataPoint> revenueData = new ArrayList<>();
        monthlyRevenueMap.forEach((month, revenue) -> {
            revenueData.add(AnalyticsResponse.RevenueDataPoint.builder()
                    .month(month)
                    .amount(revenue)
                    .build());
        });

        // Get popular courses (top 5 by enrollment count)
        List<AnalyticsResponse.PopularCourseData> popularCourses = courseRepository.findAll()
                .stream()
                .sorted((c1, c2) -> Long.compare(
                        c2.getTotalStudents() != null ? c2.getTotalStudents() : 0L,
                        c1.getTotalStudents() != null ? c1.getTotalStudents() : 0L))
                .limit(5)
                .map(course -> AnalyticsResponse.PopularCourseData.builder()
                        .courseName(course.getTitle())
                        .studentCount(course.getTotalStudents() != null ? course.getTotalStudents() : 0L)
                        .build())
                .toList();

        // Get student growth (last 6 months)
        Map<String, Long> monthlyGrowthMap = enrollmentService.getMonthlyStudentGrowth(6);
        List<AnalyticsResponse.StudentGrowthData> studentGrowth = new ArrayList<>();
        monthlyGrowthMap.forEach((month, students) -> {
            studentGrowth.add(AnalyticsResponse.StudentGrowthData.builder()
                    .month(month)
                    .count(students)
                    .build());
        });

        return AnalyticsResponse.builder()
                .stats(stats)
                .revenueOverTime(revenueData)
                .popularCourses(popularCourses)
                .studentGrowth(studentGrowth)
                .build();
    }
}
