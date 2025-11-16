package com.tobi.MusicLearn_Studio_Backend.modules.auth.repository;

import com.tobi.MusicLearn_Studio_Backend.common.repository.BaseRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByUsernameAndIsDeletedFalse(String username);

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    Optional<User> findByUsernameOrEmailAndIsDeletedFalse(String username, String email);

    Boolean existsByUsernameAndIsDeletedFalse(String username);

    Boolean existsByEmailAndIsDeletedFalse(String email);
}
