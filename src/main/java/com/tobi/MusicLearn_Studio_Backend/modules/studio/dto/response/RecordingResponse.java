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
public class RecordingResponse {

    private String id;
    private String projectId;
    private String userId;
    private String fileUrl;
    private String fileName;
    private Double duration;
    private Integer sampleRate;
    private String format;
    private Long fileSize;
    private LocalDateTime createdAt;
}
