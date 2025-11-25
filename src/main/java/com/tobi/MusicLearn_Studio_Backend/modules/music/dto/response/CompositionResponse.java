package com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response;

import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicComposition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompositionResponse {

    private String id;

    private String userId;

    private String title;

    private String description;

    private MusicComposition.CompositionStatus status;

    private Integer bpm;

    private String key;

    private String scale;

    private List<MusicComposition.Track> tracks;

    private String audioFileUrl;

    private String coverImageUrl;

    private Double duration;

    private Long fileSize;

    private Boolean isPublic;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static CompositionResponse fromEntity(MusicComposition composition) {
        return CompositionResponse.builder()
                .id(composition.getId())
                .userId(composition.getUserId())
                .title(composition.getTitle())
                .description(composition.getDescription())
                .status(composition.getStatus())
                .bpm(composition.getBpm())
                .key(composition.getKey())
                .scale(composition.getScale())
                .tracks(composition.getTracks())
                .audioFileUrl(composition.getAudioFileUrl())
                .coverImageUrl(composition.getCoverImageUrl())
                .duration(composition.getDuration())
                .fileSize(composition.getFileSize())
                .isPublic(composition.getIsPublic())
                .createdAt(composition.getCreatedAt())
                .updatedAt(composition.getUpdatedAt())
                .build();
    }
}
