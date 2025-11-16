package com.tobi.MusicLearn_Studio_Backend.common.repository;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends MongoRepository<T, String> {

    // Find one by ID (excluding soft deleted)
    Optional<T> findByIdAndIsDeletedFalse(String id);

    // Find all (excluding soft deleted)
    List<T> findAllByIsDeletedFalse();

    // Find all with pagination (excluding soft deleted)
    Page<T> findAllByIsDeletedFalse(Pageable pageable);

    // Count non-deleted records
    long countByIsDeletedFalse();

    // Check if exists and not deleted
    boolean existsByIdAndIsDeletedFalse(String id);
}
