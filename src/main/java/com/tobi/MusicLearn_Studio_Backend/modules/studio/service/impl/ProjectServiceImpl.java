package com.tobi.MusicLearn_Studio_Backend.modules.studio.service.impl;

import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.request.CreateProjectRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.request.UpdateProjectRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.response.ProjectResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.response.ProjectWithTracksResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.dto.response.TrackResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.entity.Project;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.entity.Track;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.repository.ProjectRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.repository.TrackRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TrackRepository trackRepository;

    @Override
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request, String userId) {
        Project project = Project.builder()
                .name(request.getName())
                .userId(userId)
                .tempo(request.getTempo())
                .keySignature(request.getKeySignature())
                .scaleType(request.getScaleType())
                .timeSignature(request.getTimeSignature())
                .duration(0.0)
                .trackCount(0)
                .build();

        project.setCreatedBy(userId);
        Project savedProject = projectRepository.save(project);

        return mapToResponse(savedProject);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(String id, UpdateProjectRequest request, String userId) {
        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (request.getName() != null) {
            project.setName(request.getName());
        }
        if (request.getTempo() != null) {
            project.setTempo(request.getTempo());
        }
        if (request.getKeySignature() != null) {
            project.setKeySignature(request.getKeySignature());
        }
        if (request.getScaleType() != null) {
            project.setScaleType(request.getScaleType());
        }
        if (request.getTimeSignature() != null) {
            project.setTimeSignature(request.getTimeSignature());
        }
        if (request.getDuration() != null) {
            project.setDuration(request.getDuration());
        }

        project.setUpdatedBy(userId);
        Project updatedProject = projectRepository.save(project);

        return mapToResponse(updatedProject);
    }

    @Override
    @Transactional
    public void deleteProject(String id, String userId) {
        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        // Soft delete related tracks
        List<Track> tracks = trackRepository.findByProjectIdAndIsDeletedFalse(id);
        tracks.forEach(track -> {
            track.setIsDeleted(true);
            track.setUpdatedBy(userId);
        });
        trackRepository.saveAll(tracks);

        // Soft delete project
        project.setIsDeleted(true);
        project.setUpdatedBy(userId);
        projectRepository.save(project);
    }

    @Override
    public ProjectResponse getProjectById(String id) {
        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        return mapToResponse(project);
    }

    @Override
    public ProjectWithTracksResponse getProjectWithTracks(String id) {
        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        List<Track> tracks = trackRepository.findByProjectIdAndIsDeletedFalseOrderByTrackNumberAsc(id);

        List<TrackResponse> trackResponses = tracks.stream()
                .map(this::mapTrackToResponse)
                .toList();

        return ProjectWithTracksResponse.builder()
                .project(mapToResponse(project))
                .tracks(trackResponses)
                .build();
    }

    @Override
    public List<ProjectResponse> getUserProjects(String userId) {
        List<Project> projects = projectRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId);
        return projects.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProjectResponse mapToResponse(Project project) {
        long trackCount = trackRepository.countByProjectIdAndIsDeletedFalse(project.getId());

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .userId(project.getUserId())
                .tempo(project.getTempo())
                .keySignature(project.getKeySignature())
                .scaleType(project.getScaleType())
                .timeSignature(project.getTimeSignature())
                .duration(project.getDuration())
                .trackCount((int) trackCount)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    private TrackResponse mapTrackToResponse(Track track) {
        return TrackResponse.builder()
                .id(track.getId())
                .projectId(track.getProjectId())
                .trackNumber(track.getTrackNumber())
                .name(track.getName())
                .instrumentType(track.getInstrumentType())
                .notesData(track.getNotesData())
                .volume(track.getVolume())
                .pan(track.getPan())
                .muted(track.getMuted())
                .solo(track.getSolo())
                .createdAt(track.getCreatedAt())
                .updatedAt(track.getUpdatedAt())
                .build();
    }
}
