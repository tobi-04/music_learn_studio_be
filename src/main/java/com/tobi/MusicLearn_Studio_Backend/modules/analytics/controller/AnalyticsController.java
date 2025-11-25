package com.tobi.MusicLearn_Studio_Backend.modules.analytics.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.analytics.dto.response.AnalyticsResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.analytics.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "APIs for analytics and statistics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    @Operation(summary = "Get analytics data")
    public ResponseEntity<BaseResponse<AnalyticsResponse>> getAnalytics(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {

        AnalyticsResponse analytics = analyticsService.getAnalytics();

        return ResponseEntity.ok(BaseResponse.<AnalyticsResponse>builder()
                .success(true)
                .message("Analytics retrieved successfully")
                .data(analytics)
                .build());
    }
}
