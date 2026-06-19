# Campus Collaboration Platform

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen?style=for-the-badge&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![JWT](https://img.shields.io/badge/JWT-0.12.5-red?style=for-the-badge&logo=JSON%20web%20tokens)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**A secure, full-featured backend platform for campus collaboration with JWT authentication, role-based access control, and comprehensive API documentation.**

[Features](#-features) • [Tech Stack](#-tech-stack) • [Architecture](#-project-architecture) • [API Documentation](#-api-documentation) • [Setup](#-setup-and-installation)

</div>

---

## 📋 Project Overview

The Campus Collaboration Platform is a robust, production-ready backend application designed to facilitate seamless collaboration among students and faculty. Built with modern Spring Boot architecture, it provides a secure environment for users to create, share, and interact with content while maintaining strict security standards through JWT-based authentication and authorization.

This project demonstrates expertise in:
- **Enterprise-grade application development** with Spring Boot
- **Security implementation** using JWT tokens and role-based access control
- **RESTful API design** following industry best practices
- **Database management** with JPA/Hibernate and MySQL
- **API documentation** with OpenAPI/Swagger
- **Clean architecture** with separation of concerns

---

## ✨ Features

### Core Functionality
- **User Authentication & Authorization**
  - JWT-based secure authentication
  - Role-based access control (ROLE_USER, ROLE_ADMIN)
  - Password encryption with BCrypt
  - User registration and login

- **Post Management**
  - Create, read, update, and delete posts
  - Paginated post retrieval
  - Search posts by keyword (title and content)
  - Owner-only edit/delete permissions

- **Comment System**
  - Add comments to posts
  - Delete own comments
  - Retrieve comments for specific posts

- **Like System**
  - Like/unlike posts
  - Get like count for posts
  - Check if user has liked a post
  - Unique constraint prevents duplicate likes

- **Admin Module**
  - View all users (without exposing passwords)
  - Delete users with cascade deletion (likes, comments, posts)
  - Delete any post
  - Update user roles
  - View platform statistics (users, posts, comments, likes)

### Advanced Features
- **Global Exception Handling**
  - Standardized error responses
  - Custom exceptions (ResourceNotFoundException, UnauthorizedException)
  - Validation error handling

- **API Documentation**
  - Interactive Swagger UI
  - JWT authentication support in Swagger
  - Comprehensive endpoint documentation

- **Security**
  - Method-level security with @PreAuthorize
  - JWT token validation
  - Protected endpoints for authenticated users
  - Admin-only endpoints

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17 | Core language |
| **Spring Boot** | 3.3.0 | Application framework |
| **Spring Security** | 3.3.0 | Authentication & authorization |
| **Spring Data JPA** | 3.3.0 | Database ORM |
| **MySQL** | 8.0 | Relational database |
| **JWT (jjwt)** | 0.12.5 | Token-based authentication |
| **SpringDoc OpenAPI** | 2.5.0 | API documentation |
| **Lombok** | Latest | Reduce boilerplate code |
| **Maven** | Latest | Build tool |
| **Jakarta Validation** | Latest | Request validation |

---

## 🏗 Project Architecture

The project follows a **layered architecture** pattern with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                     Controller Layer                         │
│  (REST Endpoints, Request Validation, Response Handling)    │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                      Service Layer                           │
│  (Business Logic, Transaction Management, Authorization)    │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    Repository Layer                          │
│  (Data Access, JPA Operations, Custom Queries)               │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    Database Layer                             │
│  (MySQL Database, Tables, Relationships)                    │
└─────────────────────────────────────────────────────────────┘
```

### Package Structure

```
com.campus.collab/
├── config/              # Configuration classes
│   ├── SecurityConfig   # Spring Security configuration
│   └── OpenApiConfig    # Swagger/OpenAPI configuration
├── controller/          # REST controllers
│   ├── AuthController   # Authentication endpoints
│   ├── UserController   # User management
│   ├── PostController   # Post CRUD operations
│   ├── CommentController # Comment operations
│   ├── LikeController   # Like operations
│   └── AdminController  # Admin-specific operations
├── dto/                 # Data Transfer Objects
│   ├── *Request         # Request DTOs
│   └── *Response        # Response DTOs
├── exception/           # Exception handling
│   ├── GlobalExceptionHandler
│   ├── ResourceNotFoundException
│   └── UnauthorizedException
├── model/               # JPA Entities
│   ├── User
│   ├── Post
│   ├── Comment
│   └── Like
├── repository/          # Data access layer
│   ├── UserRepository
│   ├── PostRepository
│   ├── CommentRepository
│   └── LikeRepository
├── security/            # Security components
│   ├── JwtUtils         # JWT token utilities
│   ├── JwtAuthenticationFilter
│   ├── UserDetailsImpl
│   └── UserDetailsServiceImpl
└── service/             # Business logic layer
    ├── AuthService
    ├── PostService
    ├── CommentService
    ├── LikeService
    └── AdminService
```

---

## 🗄 Database Entities

### User
- **Fields**: id, username, email, password, role
- **Roles**: ROLE_USER, ROLE_ADMIN
- **Relationships**: One-to-many with Posts, Comments, and Likes

### Post
- **Fields**: id, title, content, createdAt, updatedAt
- **Relationships**: Many-to-one with User (author), One-to-many with Comments, One-to-many with Likes

### Comment
- **Fields**: id, content, createdAt
- **Relationships**: Many-to-one with User (author), Many-to-one with Post

### Like
- **Fields**: id
- **Relationships**: Many-to-one with User, Many-to-one with Post
- **Constraint**: Unique constraint on (user_id, post_id) to prevent duplicate likes

---

## 🔐 Security Flow

### JWT Authentication & Authorization

```
┌──────────────┐
│   User       │
└──────┬───────┘
       │
       │ 1. Login Request
       ↓
┌─────────────────────────────────────┐
│      AuthController                 │
│  - Validates credentials            │
│  - Generates JWT token              │
└──────┬──────────────────────────────┘
       │
       │ 2. Returns JWT Token
       ↓
┌─────────────────────────────────────┐
│         Client                      │
│  - Stores JWT token                 │
└──────┬──────────────────────────────┘
       │
       │ 3. Protected API Request
       │    (with JWT in Authorization header)
       ↓
┌─────────────────────────────────────┐
│   JwtAuthenticationFilter           │
│  - Extracts JWT from header         │
│  - Validates token                  │
│  - Sets authentication in context   │
└──────┬──────────────────────────────┘
       │
       │ 4. Authentication successful
       ↓
┌─────────────────────────────────────┐
│      Controller/Service            │
│  - @PreAuthorize checks roles       │
│  - Executes business logic          │
└─────────────────────────────────────┘
```

### Security Features
- **JWT Token Expiration**: Configurable token lifetime
- **Password Encryption**: BCrypt hashing
- **Role-Based Access**: Method-level security with @PreAuthorize
- **Public Endpoints**: Registration, login, and public content access
- **Protected Endpoints**: All user-specific operations require JWT
- **Admin Endpoints**: Only accessible to users with ROLE_ADMIN

---

## 📚 API Documentation

The project includes comprehensive API documentation using **SpringDoc OpenAPI**.

### Access Swagger UI
- **Local**: `http://localhost:9004/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:9004/v3/api-docs`

### Using JWT in Swagger
1. Click the **"Authorize"** button in Swagger UI
2. Enter your JWT token (obtained from login endpoint)
3. Click **"Authorize"** to authenticate
4. All subsequent requests will include the JWT token

### API Endpoints

#### Authentication
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/login` - Login and receive JWT token

#### Posts
- `GET /api/posts` - Get all posts (paginated)
- `GET /api/posts/search?keyword={keyword}` - Search posts
- `GET /api/posts/my` - Get current user's posts
- `GET /api/posts/{id}` - Get post by ID
- `POST /api/posts` - Create new post
- `PUT /api/posts/{id}` - Update post (owner only)
- `DELETE /api/posts/{id}` - Delete post (owner only)

#### Comments
- `GET /api/posts/{postId}/comments` - Get comments for a post
- `POST /api/posts/{postId}/comments` - Add comment to post
- `DELETE /api/comments/{commentId}` - Delete comment (owner only)

#### Likes
- `POST /api/posts/{postId}/like` - Like a post
- `DELETE /api/posts/{postId}/like` - Unlike a post
- `GET /api/posts/{postId}/likes/count` - Get like count
- `GET /api/posts/{postId}/likes/me` - Check if user liked post

#### Admin (ROLE_ADMIN only)
- `GET /api/admin/users` - Get all users
- `DELETE /api/admin/users/{id}` - Delete user
- `DELETE /api/admin/posts/{id}` - Delete any post
- `PUT /api/admin/users/{id}/role` - Update user role
- `GET /api/admin/stats` - Get platform statistics

---

## 👑 Admin Module Features

The admin module provides comprehensive management capabilities for platform administrators:

### User Management
- **View All Users**: Retrieve list of all registered users without exposing passwords
- **Delete Users**: Remove users with automatic cascade deletion of:
  - All likes created by the user
  - All comments created by the user
  - All posts created by the user
  - Finally, the user record itself
- **Update User Roles**: Change user role between ROLE_USER and ROLE_ADMIN

### Content Management
- **Delete Any Post**: Admins can delete any post regardless of ownership
- **View Statistics**: Get platform-wide statistics including:
  - Total users count
  - Total posts count
  - Total comments count
  - Total likes count

### Security
- All admin endpoints protected with `@PreAuthorize("hasRole('ADMIN')")`
- Method-level security ensures only admins can access admin functions
- Transaction support ensures data consistency during cascade deletions

---

## 🚀 Setup and Installation

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

### Clone the Repository
```bash
git clone https://github.com/your-username/Campus-collab.git
cd Campus-collab
```

### Database Setup

1. **Create MySQL Database**
```sql
CREATE DATABASE campus_collab;
```

2. **Configure Database Connection**

The project uses environment variables for sensitive configuration. Copy the example file and customize it:

```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

Edit `src/main/resources/application.properties` with your actual values:
```properties
# Server Configuration
server.port=9004

# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/campus_collab?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT Configuration
jwt.secret=your_jwt_secret_key_here
jwt.expiration=86400000

# Logging
logging.level.org.springframework.web=INFO
logging.level.org.hibernate=INFO
```

### Environment Variables

For production or to avoid committing secrets, use environment variables. The application.properties file uses environment variables with default values:

```bash
export DB_URL=jdbc:mysql://localhost:3306/campus_collab?createDatabaseIfNotExist=true
export DB_USERNAME=root
export DB_PASSWORD=your_password
export JWT_SECRET=your_jwt_secret_key
export JWT_EXPIRATION=86400000
```

**Note**: The `application.properties` file is included in `.gitignore` to prevent committing sensitive information. Use `application-example.properties` as a template for your local configuration.

### Build the Project
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/collab-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:9004`

---

## 🧪 Testing the API

### Using Swagger UI
1. Navigate to `http://localhost:9004/swagger-ui.html`
2. Use the signup endpoint to create a user
3. Use the login endpoint to get a JWT token
4. Click "Authorize" and enter your JWT token
5. Test all endpoints through the interactive UI

### Using cURL

**Signup:**
```bash
curl -X POST http://localhost:9004/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:9004/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

**Create Post:**
```bash
curl -X POST http://localhost:9004/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "My First Post",
    "content": "This is the content of my post"
  }'
```

---

## 🔮 Future Enhancements

### Planned Features
- **Email Verification**: Implement email verification for user registration
- **Password Reset**: Add forgot password functionality
- **File Upload**: Support for image and file attachments in posts
- **Real-time Notifications**: WebSocket-based notifications
- **User Profiles**: Enhanced user profile management
- **Post Categories**: Categorize posts for better organization
- **Search Enhancement**: Advanced search with filters and sorting
- **Rate Limiting**: Implement API rate limiting for security
- **Caching**: Add Redis caching for improved performance
- **Audit Logging**: Track user actions for compliance
- **Unit Tests**: Comprehensive unit and integration tests
- **Docker Support**: Containerize the application for easy deployment

### Scalability Improvements
- **Microservices Architecture**: Split into separate services
- **Message Queue**: Implement RabbitMQ/Kafka for async processing
- **Load Balancing**: Support for horizontal scaling
- **Database Optimization**: Add indexing and query optimization

---

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 👨‍💻 Author

**Shravya Jakkani**
---



<div align="center">

**Built with ❤️ using Spring Boot**

[⬆ Back to Top](#campus-collaboration-platform)

</div>
