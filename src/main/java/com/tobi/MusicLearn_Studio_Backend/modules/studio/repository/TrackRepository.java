package com.tobi.MusicLearn_Studio_Backend.modules.studio.repository;

import com.tobi.MusicLearn_Studio_Backend.common.repository.BaseRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.entity.Track;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends BaseRepository<Track> {

    List<Track> findByProjectIdAndIsDeletedFalse(String projectId);

    List<Track> findByProjectIdAndIsDeletedFalseOrderByTrackNumberAsc(String projectId);

    long countByProjectIdAndIsDeletedFalse(String projectId);
}
