package com.tobi.MusicLearn_Studio_Backend.modules.studio.repository;

import com.tobi.MusicLearn_Studio_Backend.common.repository.BaseRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.entity.Recording;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordingRepository extends BaseRepository<Recording> {

    List<Recording> findByProjectIdAndIsDeletedFalse(String projectId);

    List<Recording> findByUserIdAndIsDeletedFalse(String userId);
}
