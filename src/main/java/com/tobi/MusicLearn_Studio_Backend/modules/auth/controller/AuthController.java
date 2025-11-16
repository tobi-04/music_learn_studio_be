package com.tobi.MusicLearn_Studio_Backend.modules.auth.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.common.dto.PageResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.request.LoginRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.request.RegisterRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.request.UpdateUserRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.response.AuthResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.dto.response.UserResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication & User Management", description = "API quản lý xác thực và người dùng")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Đăng ký tài khoản mới", description = "Tạo tài khoản người dùng mới với thông tin đầy đủ. Username và email phải unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Đăng ký thành công", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "409", description = "Username hoặc email đã tồn tại")
    })
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success("User registered successfully", response));
    }

    @Operation(summary = "Đăng nhập", description = "Đăng nhập bằng username/email và password. Trả về JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đăng nhập thành công", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Thông tin đăng nhập không chính xác"),
            @ApiResponse(responseCode = "403", description = "Tài khoản đã bị khóa")
    })
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(BaseResponse.success("Login successful", response));
    }

    @Operation(summary = "Tạo nhiều users cùng lúc", description = "Bulk create users - hữu ích cho việc import dữ liệu")
    @ApiResponse(responseCode = "201", description = "Tạo users thành công")
    @PostMapping("/users/bulk")
    public ResponseEntity<BaseResponse<List<UserResponse>>> createMany(
            @Valid @RequestBody List<RegisterRequest> requests) {
        List<UserResponse> response = authService.createMany(requests);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success("Users created successfully", response));
    }

    @Operation(summary = "Lấy thông tin người dùng theo ID", description = "Trả về thông tin chi tiết của một người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<BaseResponse<UserResponse>> getUserById(
            @Parameter(description = "ID của người dùng", required = true) @PathVariable String id) {
        UserResponse response = authService.findById(id);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(summary = "Lấy danh sách tất cả người dùng", description = "Trả về danh sách tất cả người dùng trong hệ thống (không phân trang)")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @GetMapping("/users")
    public ResponseEntity<BaseResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> response = authService.findAll();
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(summary = "Lấy danh sách người dùng có phân trang", description = "Trả về danh sách người dùng với phân trang và sắp xếp")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @GetMapping("/users/paginate")
    public ResponseEntity<BaseResponse<PageResponse<UserResponse>>> getUsersWithPagination(
            @Parameter(description = "Số trang (bắt đầu từ 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items mỗi trang", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường sắp xếp", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Hướng sắp xếp (asc/desc)", example = "desc") @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        PageResponse<UserResponse> response = authService.findAll(pageable);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(summary = "Cập nhật thông tin người dùng", description = "Cập nhật thông tin cá nhân của người dùng (name, email, avatar, isLocked)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng"),
            @ApiResponse(responseCode = "409", description = "Email đã tồn tại")
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<BaseResponse<UserResponse>> updateUser(
            @Parameter(description = "ID của người dùng", required = true) @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = authService.update(id, request);
        return ResponseEntity.ok(BaseResponse.success("User updated successfully", response));
    }

    @Operation(summary = "Cập nhật nhiều users cùng lúc", description = "Bulk update users - cập nhật cùng một thông tin cho nhiều users")
    @ApiResponse(responseCode = "200", description = "Cập nhật thành công")
    @PutMapping("/users/bulk")
    public ResponseEntity<BaseResponse<List<UserResponse>>> updateMany(
            @Parameter(description = "Danh sách IDs cần cập nhật") @RequestParam List<String> ids,
            @Valid @RequestBody UpdateUserRequest request) {
        List<UserResponse> response = authService.updateMany(ids, request);
        return ResponseEntity.ok(BaseResponse.success("Users updated successfully", response));
    }

    @Operation(summary = "Xóa người dùng (Soft Delete)", description = "Đánh dấu người dùng là đã xóa (isDeleted = true) nhưng không xóa khỏi database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteUser(
            @Parameter(description = "ID của người dùng", required = true) @PathVariable String id) {
        authService.delete(id);
        return ResponseEntity.ok(BaseResponse.success("User deleted successfully", null));
    }

    @Operation(summary = "Xóa nhiều users cùng lúc (Soft Delete)", description = "Bulk soft delete users")
    @ApiResponse(responseCode = "200", description = "Xóa thành công")
    @DeleteMapping("/users/bulk")
    public ResponseEntity<BaseResponse<Void>> deleteMany(
            @Parameter(description = "Danh sách IDs cần xóa") @RequestParam List<String> ids) {
        authService.deleteMany(ids);
        return ResponseEntity.ok(BaseResponse.success("Users deleted successfully", null));
    }

    @Operation(summary = "Xóa vĩnh viễn người dùng (Hard Delete)", description = "Xóa người dùng hoàn toàn khỏi database - KHÔNG THỂ KHÔI PHỤC")
    @ApiResponse(responseCode = "200", description = "Xóa vĩnh viễn thành công")
    @DeleteMapping("/users/{id}/permanent")
    public ResponseEntity<BaseResponse<Void>> hardDeleteUser(
            @Parameter(description = "ID của người dùng", required = true) @PathVariable String id) {
        authService.hardDelete(id);
        return ResponseEntity.ok(BaseResponse.success("User permanently deleted", null));
    }

    @Operation(summary = "Khóa tài khoản người dùng", description = "Khóa tài khoản để ngăn người dùng đăng nhập (isLocked = true)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Khóa tài khoản thành công", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    @PatchMapping("/users/{id}/lock")
    public ResponseEntity<BaseResponse<UserResponse>> lockUser(
            @Parameter(description = "ID của người dùng", required = true) @PathVariable String id) {
        UserResponse response = authService.lockUser(id);
        return ResponseEntity.ok(BaseResponse.success("User locked successfully", response));
    }

    @Operation(summary = "Mở khóa tài khoản người dùng", description = "Mở khóa tài khoản đã bị khóa (isLocked = false)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mở khóa thành công", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    @PatchMapping("/users/{id}/unlock")
    public ResponseEntity<BaseResponse<UserResponse>> unlockUser(
            @Parameter(description = "ID của người dùng", required = true) @PathVariable String id) {
        UserResponse response = authService.unlockUser(id);
        return ResponseEntity.ok(BaseResponse.success("User unlocked successfully", response));
    }

    @Operation(summary = "Kiểm tra user có tồn tại không", description = "Kiểm tra xem user ID có tồn tại trong hệ thống không")
    @ApiResponse(responseCode = "200", description = "Trả về true/false")
    @GetMapping("/users/{id}/exists")
    public ResponseEntity<BaseResponse<Boolean>> checkUserExists(
            @Parameter(description = "ID của người dùng", required = true) @PathVariable String id) {
        boolean exists = authService.exists(id);
        return ResponseEntity.ok(BaseResponse.success(exists));
    }
}
