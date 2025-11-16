package com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dữ liệu cập nhật thông tin người dùng")
public class UpdateUserRequest {

    @Schema(description = "Tên đầy đủ", example = "Nguyễn Văn B")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Email mới", example = "newmail@example.com")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "URL ảnh đại diện mới", example = "https://example.com/new-avatar.jpg")
    private String avatar;

    @Schema(description = "Trạng thái khóa tài khoản", example = "false")
    private Boolean isLocked;
}
