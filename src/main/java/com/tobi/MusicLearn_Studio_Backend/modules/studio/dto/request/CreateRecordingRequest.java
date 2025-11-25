package com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.request;

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
public class CreateRecordingRequest {

    @NotBlank(message = "Project ID is required")
    private String projectId;

    @NotBlank(message = "File name is required")
    private String fileName;

    private Double duration; // Duration in seconds

    @NotNull(message = "Sample rate is required")
    private Integer sampleRate; // 44100, 48000, etc.

    @NotBlank(message = "Format is required")
    private String format; // webm, wav, mp3

    private Double gain; // Gain/volume adjustment in dB
}
