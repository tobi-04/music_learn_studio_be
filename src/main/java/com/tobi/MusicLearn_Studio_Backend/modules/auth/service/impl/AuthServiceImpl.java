package com.tobi.MusicLearn_Studio_Backend.modules.auth.service.impl;

import com.tobi.MusicLearn_Studio_Backend.common.dto.PageResponse;
import com.tobi.MusicLearn_Studio_Backend.common.exceptions.BadRequestException;
import com.tobi.MusicLearn_Studio_Backend.common.exceptions.DuplicateResourceException;
import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import com.tobi.MusicLearn_Studio_Backend.common.security.JwtTokenProvider;
import com.tobi.MusicLearn_Studio_Backend.common.utils.PageUtils;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.request.LoginRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.request.RegisterRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.request.UpdateUserRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.response.AuthResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.response.UserResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.entity.User;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.repository.UserRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with username: {}", request.getUsername());

        // Validate duplicate username
        if (userRepository.existsByUsernameAndIsDeletedFalse(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }

        // Validate duplicate email
        if (userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        // Create new user
        User user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .avatar(request.getAvatar())
                .isLocked(false)
                .role("USER")
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        String token = jwtTokenProvider.generateToken(savedUser.getId());

        return AuthResponse.builder()
                .token(token)
                .user(convertToResponse(savedUser))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("User login attempt: {}", request.getUsernameOrEmail());

        User user = userRepository.findByUsernameOrEmailAndIsDeletedFalse(
                request.getUsernameOrEmail(),
                request.getUsernameOrEmail()).orElseThrow(
                        () -> new ResourceNotFoundException("User", "username/email", request.getUsernameOrEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        if (user.getIsLocked()) {
            throw new BadRequestException("Account is locked");
        }

        log.info("User logged in successfully: {}", user.getUsername());

        String token = jwtTokenProvider.generateToken(user.getId());

        return AuthResponse.builder()
                .token(token)
                .user(convertToResponse(user))
                .build();
    }

    @Override
    public UserResponse create(RegisterRequest request) {
        return register(request).getUser();
    }

    @Override
    @Transactional
    public List<UserResponse> createMany(List<RegisterRequest> requests) {
        log.info("Creating {} users", requests.size());

        List<User> users = requests.stream()
                .map(request -> {
                    if (userRepository.existsByUsernameAndIsDeletedFalse(request.getUsername())) {
                        throw new DuplicateResourceException("User", "username", request.getUsername());
                    }
                    if (userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
                        throw new DuplicateResourceException("User", "email", request.getEmail());
                    }

                    return User.builder()
                            .name(request.getName())
                            .username(request.getUsername())
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .avatar(request.getAvatar())
                            .isLocked(false)
                            .role("USER")
                            .build();
                })
                .collect(Collectors.toList());

        List<User> savedUsers = userRepository.saveAll(users);
        log.info("Created {} users successfully", savedUsers.size());

        return savedUsers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(String id) {
        log.info("Finding user by ID: {}", id);

        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return convertToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        log.info("Finding all users");

        return userRepository.findAllByIsDeletedFalse().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> findAll(Pageable pageable) {
        log.info("Finding users with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<User> userPage = userRepository.findAllByIsDeletedFalse(pageable);

        return PageUtils.toPageResponse(userPage, this::convertToResponse);
    }

    @Override
    @Transactional
    public UserResponse update(String id, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);

        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Update name
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        // Update email
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!request.getEmail().equals(user.getEmail()) &&
                    userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
                throw new DuplicateResourceException("User", "email", request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        // Update avatar
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        // Update isLocked
        if (request.getIsLocked() != null) {
            user.setIsLocked(request.getIsLocked());
        }

        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        log.info("User updated successfully: {}", updatedUser.getId());

        return convertToResponse(updatedUser);
    }

    @Override
    @Transactional
    public List<UserResponse> updateMany(List<String> ids, UpdateUserRequest request) {
        log.info("Updating {} users", ids.size());

        List<User> users = ids.stream()
                .map(id -> userRepository.findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "id", id)))
                .peek(user -> {
                    if (request.getName() != null)
                        user.setName(request.getName());
                    if (request.getAvatar() != null)
                        user.setAvatar(request.getAvatar());
                    if (request.getIsLocked() != null)
                        user.setIsLocked(request.getIsLocked());
                    user.setUpdatedAt(LocalDateTime.now());
                })
                .collect(Collectors.toList());

        List<User> savedUsers = userRepository.saveAll(users);
        log.info("Updated {} users successfully", savedUsers.size());

        return savedUsers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String id) {
        log.info("Soft deleting user with ID: {}", id);

        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setIsDeleted(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User soft deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteMany(List<String> ids) {
        log.info("Soft deleting {} users", ids.size());

        List<User> users = ids.stream()
                .map(id -> userRepository.findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "id", id)))
                .peek(user -> {
                    user.setIsDeleted(true);
                    user.setUpdatedAt(LocalDateTime.now());
                })
                .collect(Collectors.toList());

        userRepository.saveAll(users);
        log.info("Soft deleted {} users successfully", users.size());
    }

    @Override
    @Transactional
    public void hardDelete(String id) {
        log.info("Hard deleting user with ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }

        userRepository.deleteById(id);
        log.info("User hard deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(String id) {
        return userRepository.existsByIdAndIsDeletedFalse(id);
    }

    @Override
    @Transactional
    public UserResponse lockUser(String id) {
        log.info("Locking user with ID: {}", id);

        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setIsLocked(true);
        user.setUpdatedAt(LocalDateTime.now());
        User lockedUser = userRepository.save(user);

        log.info("User locked successfully: {}", id);

        return convertToResponse(lockedUser);
    }

    @Override
    @Transactional
    public UserResponse unlockUser(String id) {
        log.info("Unlocking user with ID: {}", id);

        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setIsLocked(false);
        user.setUpdatedAt(LocalDateTime.now());
        User unlockedUser = userRepository.save(user);

        log.info("User unlocked successfully: {}", id);

        return convertToResponse(unlockedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String userId) {
        log.info("Getting current user with ID: {}", userId);

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (user.getIsLocked()) {
            throw new BadRequestException("User account is locked");
        }

        return convertToResponse(user);
    }

    // Converter method
    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .isLocked(user.getIsLocked())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
