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
public class TrackResponse {

    private String id;
    private String projectId;
    private Integer trackNumber;
    private String name;
    private String instrumentType;
    private String notesData;
    private Double volume;
    private Double pan;
    private Boolean muted;
    private Boolean solo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
