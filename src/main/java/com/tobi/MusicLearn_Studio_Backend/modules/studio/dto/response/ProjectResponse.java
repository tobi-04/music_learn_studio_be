package com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private String id;
    private String name;
    private String userId;
    private Integer tempo;
    private String keySignature;
    private String scaleType;
    private String timeSignature;
    private Double duration;
    private Integer trackCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
