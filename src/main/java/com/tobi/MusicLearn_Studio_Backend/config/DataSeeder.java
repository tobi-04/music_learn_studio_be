package com.tobi.MusicLearn_Studio_Backend.config;

import com.tobi.MusicLearn_Studio_Backend.modules.auth.entity.User;
import com.tobi.MusicLearn_Studio_Backend.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataSeeder - Tự động seed dữ liệu cơ bản khi ứng dụng khởi động
 * Hiện tại: Tạo admin account mặc định nếu chưa tồn tại
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.name:Admin User}")
    private String adminName;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.email:admin@musiclearn.studio}")
    private String adminEmail;

    @Value("${admin.password:Admin@123456}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        log.info("🌱 Starting data seeding...");
        seedAdminAccount();
        log.info("✅ Data seeding completed!");
    }

    /**
     * Seed admin account nếu chưa tồn tại
     */
    private void seedAdminAccount() {
        try {
            // Check nếu admin đã tồn tại
            if (userRepository.existsByUsernameAndIsDeletedFalse(adminUsername)) {
                log.info("👤 Admin account already exists: {}", adminUsername);
                return;
            }

            if (userRepository.existsByEmailAndIsDeletedFalse(adminEmail)) {
                log.info("👤 Admin account with email already exists: {}", adminEmail);
                return;
            }

            // Tạo admin account mới
            User admin = User.builder()
                    .name(adminName)
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role("ADMIN")
                    .isLocked(false)
                    .build();

            userRepository.save(admin);

            log.info("✨ Admin account created successfully!");
            log.info("   📧 Email: {}", adminEmail);
            log.info("   👤 Username: {}", adminUsername);
            log.info("   🔑 Password: {}", adminPassword);
            log.info("   ⚠️  Please change the default password after first login!");

        } catch (Exception e) {
            log.error("❌ Failed to seed admin account: {}", e.getMessage(), e);
        }
    }
}
