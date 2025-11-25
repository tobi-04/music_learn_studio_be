package com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMusicTrackRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "File URL is required")
    private String fileUrl;

    private String coverImageUrl;

    @NotNull(message = "Duration is required")
    private Double duration; // in minutes

    @NotNull(message = "File size is required")
    private Long fileSize; // in bytes

    private String genre;

    private List<String> tags;

    @Builder.Default
    private Boolean isPublic = true;
}
