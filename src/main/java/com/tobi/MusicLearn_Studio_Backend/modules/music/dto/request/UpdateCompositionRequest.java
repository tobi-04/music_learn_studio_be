package com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request;

import com.tobi.MusicLearn_Studio_Backend.modules.music.entity.MusicComposition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompositionRequest {

    private String title;

    private String description;

    private Integer bpm;

    private String key;

    private String scale;

    private List<MusicComposition.Track> tracks;

    private Boolean isPublic;

    private String coverImageUrl;
}
