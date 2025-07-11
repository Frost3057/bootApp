# Bootiestrappi

A modern Spring Boot application built with Kotlin, featuring secure authentication and MongoDB integration.

## 🚀 Features

- Spring Boot 3.4.5 with Kotlin support
- MongoDB integration with reactive support
- Spring Security with JWT authentication
- RESTful API architecture
- Coroutines support for asynchronous operations
- Input validation
- Comprehensive test coverage

## 🛠️ Tech Stack

- **Language**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.4.5
- **Database**: MongoDB
- **Security**: Spring Security with JWT
- **Build Tool**: Gradle
- **Java Version**: 21

## 📋 Prerequisites

- JDK 21 or higher
- MongoDB instance
- Gradle (or use the included Gradle wrapper)

## 🏗️ Project Structure

```
bootiestrappi/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/proj/bootiestrappi/
│   │   │       ├── Security/        # Security configurations
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── Database/        # Database configurations
│   │   │       └── BootiestrappiApplication.kt
│   │   └── resources/              # Application properties and resources
│   └── test/                       # Test files
├── build.gradle.kts               # Project dependencies and configurations
└── settings.gradle.kts           # Project settings
```

## 🚀 Getting Started

1. Clone the repository
2. Configure your MongoDB connection in `application.properties`
3. Run the application using:
   ```bash
   ./gradlew bootRun
   ```

## 🧪 Testing

Run the test suite using:
```bash
./gradlew test
```

## 🔒 Security

The application implements JWT-based authentication with Spring Security. Make sure to:
- Configure your JWT secret key
- Set up proper security headers
- Use HTTPS in production

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the issues page.