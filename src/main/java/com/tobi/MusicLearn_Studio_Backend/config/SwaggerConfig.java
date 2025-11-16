package com.tobi.MusicLearn_Studio_Backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🎵 MusicLearn Studio API Documentation")
                        .version("1.0.0")
                        .description("""
                                ## 📖 Giới thiệu
                                API cho nền tảng học tập và sáng tạo âm nhạc - **MusicLearn Studio**.

                                Hệ thống cung cấp đầy đủ chức năng để người dùng có thể:
                                - Học nhạc lý và kỹ thuật âm nhạc
                                - Sáng tác và ghi âm bài hát
                                - Chia sẻ tác phẩm với cộng đồng

                                ## 🚀 Chức năng chính

                                ### 🔐 Authentication & User Management
                                - Đăng ký, đăng nhập, quản lý người dùng
                                - Hỗ trợ CRUD đầy đủ (Create, Read, Update, Delete)
                                - Soft delete & hard delete
                                - Bulk operations (createMany, updateMany, deleteMany)
                                - Phân trang và sắp xếp

                                ### 📚 Courses Management (Coming soon)
                                - Quản lý khóa học âm nhạc
                                - Lessons và modules

                                ### 🎵 Music Creation Studio (Coming soon)
                                - Công cụ sáng tạo và ghi âm
                                - Music library

                                ## 📋 Hướng dẫn sử dụng

                                ### 1. Đăng ký tài khoản
                                ```
                                POST /api/v1/auth/register
                                {
                                  "name": "Nguyễn Văn A",
                                  "username": "nguyenvana",
                                  "email": "nguyenvana@example.com",
                                  "password": "password123",
                                  "avatar": "https://example.com/avatar.jpg"
                                }
                                ```

                                ### 2. Đăng nhập
                                ```
                                POST /api/v1/auth/login
                                {
                                  "usernameOrEmail": "nguyenvana",
                                  "password": "password123"
                                }
                                ```

                                ### 3. Sử dụng JWT Token
                                Sau khi đăng nhập, sử dụng token trong header:
                                ```
                                Authorization: Bearer {your-jwt-token}
                                ```

                                ## 📊 Base Response Format

                                Tất cả responses đều theo format:
                                ```json
                                {
                                  "success": true,
                                  "message": "Success",
                                  "data": { ... },
                                  "timestamp": "2024-01-15T10:30:00",
                                  "status": 200
                                }
                                ```

                                ## 📄 Pagination Response

                                Endpoints có phân trang trả về format:
                                ```json
                                {
                                  "content": [...],
                                  "pageNumber": 0,
                                  "pageSize": 10,
                                  "totalElements": 100,
                                  "totalPages": 10,
                                  "first": true,
                                  "last": false,
                                  "empty": false,
                                  "nextPage": 1,
                                  "previousPage": null
                                }
                                ```

                                ## 💡 Tips & Best Practices

                                - Luôn xác thực thông tin nhập vào trước khi gửi request
                                - Lưu JWT token an toàn trên client (localStorage hoặc sessionStorage)
                                - Kiểm tra hạn sử dụng của token
                                - Xử lý các error responses một cách phù hợp

                                ## 📞 Support

                                Nếu có vấn đề, hãy liên hệ: togiap.dev@example.com
                                """)
                        .contact(new Contact()
                                .name("Tobi")
                                .email("togiap.dev@example.com")
                                .url("https://devhatebug.top"))
                        .license(new License()
                                .name("Apache License 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server")));
    }
}
