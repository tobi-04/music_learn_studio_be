package com.tobi.MusicLearn_Studio_Backend.modules.music.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMusicTrackRequest {

    private String title;
    private String description;
    private String genre;
    private List<String> tags;
    private Boolean isPublic;
}
