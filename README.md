# 🎵 MusicLearn Studio Backend

**Một nền tảng kết hợp học tập (learning) và sáng tạo âm nhạc (music creation) trong cùng một web app**

Nền tảng này cho phép người dùng vừa học nhạc lý, vừa thực hành sáng tác, ghi âm và chia sẻ bài nhạc với cộng đồng.

---

## 📋 Mục Lục

- [Tính Năng](#-tính-năng)
- [Yêu Cầu Hệ Thống](#-yêu-cầu-hệ-thống)
- [Cài Đặt](#-cài-đặt)
- [Cấu Hình](#-cấu-hình)
- [Chạy Ứng Dụng](#-chạy-ứng-dụng)
- [API Documentation](#-api-documentation)
- [Cấu Trúc Project](#-cấu-trúc-project)
- [Công Nghệ Sử Dụng](#-công-nghệ-sử-dụng)
- [Các Endpoint Chính](#-các-endpoint-chính)
- [Troubleshooting](#-troubleshooting)

---

## ✨ Tính Năng

### 🔐 Authentication & User Management
- ✅ Đăng ký tài khoản với validation
- ✅ Đăng nhập và quản lý phiên
- ✅ Soft delete & hard delete
- ✅ Khóa/mở khóa tài khoản
- ✅ Bulk operations (tạo, cập nhật, xóa nhiều users)
- ✅ Phân trang và sắp xếp

### 📚 Courses Management (Coming Soon)
- Quản lý khóa học âm nhạc
- Lesson & Module management

### 🎵 Music Creation Studio (Coming Soon)
- Công cụ sáng tạo và ghi âm
- Music library & sharing

---

## 🖥️ Yêu Cầu Hệ Thống

- **Java**: 21 LTS trở lên
- **Maven**: 3.8.9 trở lên
- **MongoDB**: 4.0+ (local hoặc cloud)
- **Git**: (optional)

### Kiểm Tra Cài Đặt

```bash
# Kiểm tra Java version
java -version

# Kiểm tra Maven
mvn -version

# Kiểm tra MongoDB (nếu cài đặt local)
mongod --version
```

---

## 📦 Cài Đặt

### 1. Clone Repository

```bash
# Sử dụng HTTPS
git clone https://github.com/tobi-04/music_learn_studio_be.git

# Sử dụng SSH
git clone git@github.com:tobi-04/music_learn_studio_be.git

# Chuyển vào thư mục project
cd MusicLearn-Studio-Backend
```

### 2. Cài Đặt Dependencies

```bash
# Cài đặt Maven dependencies
mvn clean install

# Hoặc chỉ tải dependencies (không build)
mvn dependency:resolve
```

### 3. Xác Minh Cài Đặt

```bash
# Kiểm tra project structure
mvn validate

# Compile code
mvn compile
```

---

## ⚙️ Cấu Hình

### MongoDB Configuration

#### Cách 1: Local MongoDB

1. **Cài đặt MongoDB** (nếu chưa có)
   ```bash
   # macOS
   brew install mongodb-community
   
   # Ubuntu/Debian
   sudo apt-get install mongodb-org
   
   # Windows
   # Tải từ: https://www.mongodb.com/try/download/community
   ```

2. **Khởi động MongoDB**
   ```bash
   # macOS
   brew services start mongodb-community
   
   # Ubuntu/Debian
   sudo systemctl start mongod
   
   # Windows (CMD as Admin)
   mongod
   ```

3. **Xác Minh kết nối**
   ```bash
   mongosh
   # Hoặc: mongo
   ```

#### Cách 2: MongoDB Atlas (Cloud)

1. Tạo tài khoản tại [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Tạo cluster
3. Lấy connection string
4. Cập nhật `application.properties` (xem bên dưới)

### Cập Nhật application.properties

**File:** `src/main/resources/application.properties`

#### Cho Local MongoDB:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/musiclearn_studio
spring.data.mongodb.database=musiclearn_studio
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
```

#### Cho MongoDB Atlas:
```properties
spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/musiclearn_studio
spring.data.mongodb.database=musiclearn_studio
```

#### Tùy chỉnh Server Port (mặc định: 8080):
```properties
server.port=8080
server.servlet.context-path=/
```

#### Cấu hình Logging:
```properties
logging.level.org.springframework.data.mongodb=DEBUG
logging.level.com.tobi.MusicLearn_Studio_Backend=DEBUG
```

---

## 🚀 Chạy Ứng Dụng

### Cách 1: Sử dụng Maven (Recommended)

```bash
# Khởi động ứng dụng
mvn spring-boot:run

# Hoặc với custom port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

### Cách 2: Build JAR rồi chạy

```bash
# Build JAR file
mvn clean package -DskipTests

# Chạy JAR file
java -jar target/MusicLearn-Studio-Backend-0.0.1-SNAPSHOT.jar

# Hoặc chỉ định port
java -jar target/MusicLearn-Studio-Backend-0.0.1-SNAPSHOT.jar --server.port=9090
```

### Cách 3: Sử dụng IDE

**IntelliJ IDEA / Eclipse:**
1. Right-click vào `MusicLearnStudioBackendApplication.java`
2. Chọn "Run 'MusicLearnStudioBackendApplication'"

**VS Code:**
1. Cài đặt Extension: "Extension Pack for Java"
2. Right-click vào `MusicLearnStudioBackendApplication.java`
3. Chọn "Run" từ context menu

### Xác Minh Ứng Dụng Chạy

Khi khởi động thành công, bạn sẽ thấy:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.5.7)

2024-11-17 00:25:00.000  INFO 12345 --- [  main] c.t.M.MusicLearnStudioBackendApplication : Started MusicLearnStudioBackendApplication in 4.123 seconds (process running for 4.567)
```

---

## 📚 API Documentation

### Swagger UI (Recommended)

**URL:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

✨ **Tính năng:**
- Interactive API explorer
- Try out các endpoints
- View request/response models
- Xem validation rules

### OpenAPI JSON

**URL:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Có thể import vào:
- Postman
- Insomnia
- Thunder Client

### Postman Collection

Ngoài ra, bạn có thể:
1. Vào Swagger UI
2. Click biểu tượng export
3. Import vào Postman

---

## 📁 Cấu Trúc Project

```
MusicLearn-Studio-Backend/
├── src/
│   ├── main/
│   │   ├── java/com/tobi/MusicLearn_Studio_Backend/
│   │   │   ├── common/               # Base classes, DTOs, Exceptions
│   │   │   │   ├── dto/
│   │   │   │   ├── entity/
│   │   │   │   ├── exceptions/
│   │   │   │   ├── repository/
│   │   │   │   ├── service/
│   │   │   │   └── utils/
│   │   │   ├── config/               # Spring configuration
│   │   │   ├── modules/              # Feature modules
│   │   │   │   └── auth/             # Authentication module
│   │   │   │       ├── controller/
│   │   │   │       ├── dto/
│   │   │   │       ├── entity/
│   │   │   │       ├── repository/
│   │   │   │       └── service/
│   │   │   └── MusicLearnStudioBackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                         # Unit & Integration tests
├── pom.xml                           # Maven configuration
├── README.md                         # This file
└── .gitignore
```

---

## 🛠️ Công Nghệ Sử Dụng

| Công Nghệ | Version | Mục Đích |
|-----------|---------|---------|
| Java | 21 LTS | Language |
| Spring Boot | 3.5.7 | Framework |
| Spring Data MongoDB | 3.5.7 | Database ORM |
| Spring Security | 3.5.7 | Authentication |
| Lombok | 1.18.30 | Code generation |
| SpringDoc OpenAPI | 2.8.5 | API Documentation |
| MySQL Connector | Latest | Optional SQL support |
| Maven | 3.8.9+ | Build tool |

---

## 🔌 Các Endpoint Chính

### Authentication

| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| POST | `/api/v1/auth/register` | Đăng ký tài khoản mới |
| POST | `/api/v1/auth/login` | Đăng nhập |

### User Management

| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| GET | `/api/v1/auth/users` | Lấy tất cả users |
| GET | `/api/v1/auth/users/{id}` | Lấy user theo ID |
| GET | `/api/v1/auth/users/paginate` | Lấy users với phân trang |
| PUT | `/api/v1/auth/users/{id}` | Cập nhật user |
| DELETE | `/api/v1/auth/users/{id}` | Xóa user (soft delete) |
| DELETE | `/api/v1/auth/users/{id}/permanent` | Xóa vĩnh viễn (hard delete) |
| PATCH | `/api/v1/auth/users/{id}/lock` | Khóa tài khoản |
| PATCH | `/api/v1/auth/users/{id}/unlock` | Mở khóa tài khoản |

### Bulk Operations

| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| POST | `/api/v1/auth/users/bulk` | Tạo nhiều users |
| PUT | `/api/v1/auth/users/bulk` | Cập nhật nhiều users |
| DELETE | `/api/v1/auth/users/bulk` | Xóa nhiều users |

---

## 📝 Ví Dụ Sử Dụng API

### 1. Đăng Ký

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyễn Văn A",
    "username": "nguyenvana",
    "email": "nguyenvana@example.com",
    "password": "password123",
    "avatar": "https://example.com/avatar.jpg"
  }'
```

**Response (Success):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": {
      "id": "507f1f77bcf86cd799439011",
      "name": "Nguyễn Văn A",
      "username": "nguyenvana",
      "email": "nguyenvana@example.com",
      "role": "USER",
      "createdAt": "2024-11-17T10:30:00"
    }
  },
  "timestamp": "2024-11-17T10:30:00",
  "status": 201
}
```

### 2. Đăng Nhập

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "nguyenvana",
    "password": "password123"
  }'
```

### 3. Lấy Danh Sách Users (Phân Trang)

```bash
curl -X GET "http://localhost:8080/api/v1/auth/users/paginate?page=0&size=10&sortBy=createdAt&direction=desc" \
  -H "Authorization: Bearer <your-jwt-token>"
```

---

## 🐛 Troubleshooting

### MongoDB Connection Error

**Lỗi:**
```
com.mongodb.MongoSocketOpenException: Exception opening socket
```

**Giải Pháp:**
1. Kiểm tra MongoDB đang chạy: `mongosh` hoặc `mongo`
2. Kiểm tra port: `sudo lsof -i :27017`
3. Restart MongoDB:
   ```bash
   brew services restart mongodb-community  # macOS
   sudo systemctl restart mongod            # Linux
   ```

### Port Already in Use

**Lỗi:**
```
Address already in use: bind
```

**Giải Pháp:**
```bash
# Tìm process đang dùng port 8080
sudo lsof -i :8080

# Kill process (Linux/macOS)
kill -9 <PID>

# Hoặc chạy trên port khác
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

### Build Failure

**Lỗi:**
```
[ERROR] COMPILATION ERROR
```

**Giải Pháp:**
```bash
# Clean rebuild
mvn clean install -DskipTests

# Với verbose output
mvn -X clean install -DskipTests

# Nếu vấn đề với cache Maven
rm -rf ~/.m2/repository
mvn clean install -DskipTests
```

### Java Version Mismatch

**Lỗi:**
```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.14.1:compile
```

**Giải Pháp:**
```bash
# Kiểm tra Java version
java -version

# Set JAVA_HOME (macOS)
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Verify
echo $JAVA_HOME
```

---

## 📖 Tài Liệu Thêm

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [MongoDB Official Docs](https://docs.mongodb.com/)

---

## 📞 Liên Hệ & Support

- **Email:** togiap.dev@example.com
- **Repository:** [GitHub](https://github.com/tobi-04/music_learn_studio_be)
- **Issues:** Báo cáo lỗi tại [Issues](https://github.com/tobi-04/music_learn_studio_be/issues)

---

## 📄 License

Dự án này được cấp phép dưới [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

---

## 🙏 Cảm Ơn

Cảm ơn bạn đã sử dụng MusicLearn Studio Backend!

**Happy Coding! 🎉🎵**