package com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Dữ liệu đăng nhập")
public class LoginRequest {

    @Schema(description = "Tên đăng nhập hoặc email", example = "nguyenvana")
    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;

    @Schema(description = "Mật khẩu", example = "password123")
    @NotBlank(message = "Password is required")
    private String password;
}
