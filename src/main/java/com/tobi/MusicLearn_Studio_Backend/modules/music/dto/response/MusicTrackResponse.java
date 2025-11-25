package com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response;

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
public class MusicTrackResponse {

    private String id;

    private String userId;

    private String username; // From User entity

    private String userAvatar; // From User entity

    private String title;

    private String description;

    private String fileUrl;

    private String coverImageUrl;

    private Double duration;

    private Long fileSize;

    private String waveformData;

    private String genre;

    private List<String> tags;

    private Long playCount;

    private Long likeCount;

    private Long commentCount;

    private Boolean isPublic;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
