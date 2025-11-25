package com.tobi.MusicLearn_Studio_Backend.modules.studio.repository;

import com.tobi.MusicLearn_Studio_Backend.common.repository.BaseRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.studio.entity.Project;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends BaseRepository<Project> {

    List<Project> findByUserIdAndIsDeletedFalse(String userId);

    List<Project> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(String userId);
}
