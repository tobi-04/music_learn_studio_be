package com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {

    @NotBlank(message = "Project name is required")
    private String name;

    @NotNull(message = "Tempo is required")
    @Min(value = 40, message = "Tempo must be at least 40 BPM")
    @Max(value = 300, message = "Tempo must not exceed 300 BPM")
    private Integer tempo;

    @NotBlank(message = "Key signature is required")
    private String keySignature; // C, D, E, F, G, A, B

    @NotBlank(message = "Scale type is required")
    private String scaleType; // Major, Minor, Pentatonic, etc.

    @Builder.Default
    private String timeSignature = "4/4"; // 4/4, 3/4, 6/8, etc.
}
