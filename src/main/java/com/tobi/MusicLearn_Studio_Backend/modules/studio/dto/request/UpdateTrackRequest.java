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
public class UpdateTrackRequest {

    private String name;

    private String instrumentType;

    private String notesData; // JSON string

    @Min(value = 0, message = "Volume must be between 0.0 and 1.0")
    @Max(value = 1, message = "Volume must be between 0.0 and 1.0")
    private Double volume;

    @Min(value = -1, message = "Pan must be between -1.0 and 1.0")
    @Max(value = 1, message = "Pan must be between -1.0 and 1.0")
    private Double pan;

    private Boolean muted;

    private Boolean solo;

    private Integer trackNumber;
}
