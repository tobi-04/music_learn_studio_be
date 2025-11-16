package com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin người dùng")
public class UserResponse {

    @Schema(description = "ID người dùng", example = "507f1f77bcf86cd799439011")
    private String id;

    @Schema(description = "Tên đầy đủ", example = "Nguyễn Văn A")
    private String name;

    @Schema(description = "Tên đăng nhập", example = "nguyenvana")
    private String username;

    @Schema(description = "Email", example = "nguyenvana@example.com")
    private String email;

    @Schema(description = "URL ảnh đại diện", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "Trạng thái khóa", example = "false")
    private Boolean isLocked;

    @Schema(description = "Vai trò", example = "USER")
    private String role;

    @Schema(description = "Thời gian tạo", example = "2024-01-15T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Thời gian cập nhật cuối", example = "2024-01-20T15:45:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
