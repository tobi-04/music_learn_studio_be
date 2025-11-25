package com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWithTracksResponse {

    private ProjectResponse project;
    private List<TrackResponse> tracks;
}
