package com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request;

import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicComposition;
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
public class CreateCompositionRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "BPM is required")
    private Integer bpm;

    private String key;

    private String scale;

    @Builder.Default
    private List<MusicComposition.Track> tracks = List.of();

    @Builder.Default
    private Boolean isPublic = false;
}
