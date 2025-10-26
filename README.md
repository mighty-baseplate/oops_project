#  Secure Online Examination System

> A full-stack web application demonstrating Object-Oriented Programming principles, design patterns, and enterprise Java technologies.

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

##  Overview

The **Secure Online Examination System** enables students to take online exams (MCQ, Coding, Essay) with instant feedback, while admins create exams and view analytics.

### Key Capabilities
-  Multiple exam types with automatic MCQ grading
-  Secure authentication with role-based access control
-  Real-time analytics dashboard with charts
-  Thread-safe concurrent submissions
-  Responsive mobile-friendly design

##  Features

### Student Features
- Secure login and personalized dashboard
- Take exams with real-time 30-minute timer
- Instant MCQ results with auto-grading
- Review answers with correct/wrong highlighting
- Track performance across multiple attempts

### Admin Features
- Create and manage exams using Factory Pattern
- Build question banks for different subjects
- View detailed analytics with Chart.js visualizations
- Monitor student submissions and performance
- Export results to CSV

##  Architecture

### OOP Concepts
- **Encapsulation**: Private fields, public methods
- **Inheritance**: Exam  MCQExam, CodingExam, EssayExam
- **Polymorphism**: Runtime method dispatch
- **Abstraction**: Abstract classes and interfaces

### Design Patterns
- **Factory**: Dynamic exam creation
- **Strategy**: Pluggable grading algorithms
- **Repository**: Data access abstraction
- **Template Method**: Common submission workflow

### Advanced Java
- Streams API, Collections, Concurrency
- Reflection, Serialization, Exception Handling
- Synchronized methods, ConcurrentHashMap

##  Technology Stack

**Backend**: Java 17, Spring Boot 3.2, Spring Data JPA, Spring Security, H2 Database  
**Frontend**: Thymeleaf, Bootstrap 5, Chart.js, JavaScript ES6+  
**Build**: Maven  
**Testing**: JUnit 5, 80%+ Coverage

##  Quick Start

```bash
# Clone repository
git clone https://github.com/mighty-baseplate/Krishi-coBot-eyantra25-26.git
cd Krishi-coBot-eyantra25-26

# Build and run
mvn clean install
mvn spring-boot:run

# Access at http://localhost:8080
```

### Demo Credentials
**Admin**: admin / admin123
**Students**: student1, student2, student3 / pass123

##  Project Structure

```
src/main/java/com/examsystem/
 config/           # Security configuration
 controller/       # REST endpoints
 entity/           # JPA entities
 factory/          # Factory pattern
 repository/       # Data access
 service/          # Business logic
 strategy/         # Strategy pattern
 utils/            # Utilities
```

##  Testing

```bash
mvn test                          # Run all tests
mvn test -Dtest=ExamServiceTest   # Specific test
```

##  Security

- BCrypt password hashing
- Role-based access control (RBAC)
- Spring Security authentication
- Session management
- XSS protection

##  Deployment

```bash
mvn clean package
java -jar target/online-exam-system-1.0.0.jar
```

**Production**: Switch to PostgreSQL/MySQL, enable HTTPS, configure environment variables

##  Contributing

1. Fork the repository
2. Create feature branch (git checkout -b feature/Feature)
3. Commit changes (git commit -m 'Add Feature')
4. Push to branch (git push origin feature/Feature)
5. Open Pull Request

##  License

MIT License - see [LICENSE](LICENSE) file

##  Author

**Atharva**  
GitHub: [@mighty-baseplate](https://github.com/mighty-baseplate)

##  Acknowledgments

Spring Framework, Bootstrap, Chart.js, Open Source Community

---

** Star this repo if helpful!** | *Built with  using Java & Spring Boot*
