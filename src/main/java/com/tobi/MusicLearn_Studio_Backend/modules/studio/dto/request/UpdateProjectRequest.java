package com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {

    private String name;

    @Min(value = 40, message = "Tempo must be at least 40 BPM")
    @Max(value = 300, message = "Tempo must not exceed 300 BPM")
    private Integer tempo;

    private String keySignature;

    private String scaleType;

    private String timeSignature;

    private Double duration;
}
