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
public class CreateTrackRequest {

    @NotNull(message = "Project ID is required")
    private String projectId;

    @NotBlank(message = "Track name is required")
    private String name;

    @NotBlank(message = "Instrument type is required")
    private String instrumentType; // Piano, Guitar, Drum, Bass, Synth

    @Builder.Default
    private String notesData = "[]"; // Empty JSON array initially

    @Min(value = 0, message = "Volume must be between 0.0 and 1.0")
    @Max(value = 1, message = "Volume must be between 0.0 and 1.0")
    @Builder.Default
    private Double volume = 0.8;

    @Min(value = -1, message = "Pan must be between -1.0 and 1.0")
    @Max(value = 1, message = "Pan must be between -1.0 and 1.0")
    @Builder.Default
    private Double pan = 0.0;
}
