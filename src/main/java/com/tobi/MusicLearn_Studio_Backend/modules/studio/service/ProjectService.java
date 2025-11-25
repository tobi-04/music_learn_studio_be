package com.tobi.MusicLearn_Studio_Backend.modules.studio.service;

import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.request.CreateProjectRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.request.UpdateProjectRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.response.ProjectResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.response.ProjectWithTracksResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(CreateProjectRequest request, String userId);

    ProjectResponse updateProject(String id, UpdateProjectRequest request, String userId);

    void deleteProject(String id, String userId);

    ProjectResponse getProjectById(String id);

    ProjectWithTracksResponse getProjectWithTracks(String id);

    List<ProjectResponse> getUserProjects(String userId);
}
