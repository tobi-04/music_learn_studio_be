package com.tobi.MusicLearn_Studio_Backend.modules.auth.service;

import com.tobi.MusicLearn_Studio_Backend.common.dto.PageResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.request.LoginRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.request.RegisterRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.request.UpdateUserRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.response.AuthResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthService {

    /**
     * Đăng ký tài khoản mới
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Đăng nhập
     */
    AuthResponse login(LoginRequest request);

    /**
     * Tạo user mới
     */
    UserResponse create(RegisterRequest request);

    /**
     * Tạo nhiều users
     */
    List<UserResponse> createMany(List<RegisterRequest> requests);

    /**
     * Lấy user theo ID
     */
    UserResponse findById(String id);

    /**
     * Lấy tất cả users
     */
    List<UserResponse> findAll();

    /**
     * Lấy users với phân trang
     */
    PageResponse<UserResponse> findAll(Pageable pageable);

    /**
     * Cập nhật thông tin user
     */
    UserResponse update(String id, UpdateUserRequest request);

    /**
     * Cập nhật nhiều users
     */
    List<UserResponse> updateMany(List<String> ids, UpdateUserRequest request);

    /**
     * Xóa user (soft delete)
     */
    void delete(String id);

    /**
     * Xóa nhiều users (soft delete)
     */
    void deleteMany(List<String> ids);

    /**
     * Xóa vĩnh viễn user (hard delete)
     */
    void hardDelete(String id);

    /**
     * Kiểm tra user có tồn tại không
     */
    boolean exists(String id);

    /**
     * Khóa tài khoản user
     */
    UserResponse lockUser(String id);

    /**
     * Mở khóa tài khoản user
     */
    UserResponse unlockUser(String id);

    /**
     * Lấy thông tin user hiện tại từ userId
     */
    UserResponse getCurrentUser(String userId);
}
