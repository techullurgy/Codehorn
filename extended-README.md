# 🧑‍💻 Codehorn — Online Coding & Programming Platform

> A production-oriented LeetCode-inspired coding platform built with Spring Boot, Microservices, MongoDB, React/Next.js, and modern cloud-native technologies.

Codehorn is a full-stack online coding platform where users can solve programming problems, submit solutions, execute code against test cases, track submissions, and manage their coding profiles.

The project is designed as a practical demonstration of:

- Microservices architecture (Spring Cloud Gateway, Consul Service Discovery)
- REST API design & Reactive WebFlux programming
- Distributed systems & asynchronous containerized code execution
- Authentication & authorization
- Code execution sandboxing (Docker ephemeral containers)
- Database design (MongoDB & Spring Data)
- React / Next.js application architecture
- Containerization & Kubernetes deployment (GKE, Helm, ArgoCD, Terraform)
- Observability & performance engineering

---

## 📌 Project Overview

Codehorn provides an online judge experience similar to platforms such as LeetCode.

Users can:

- Create an account & authenticate securely
- Browse programming problems
- Filter and search problems by difficulty, category, or title
- View problem descriptions, constraints, examples, and hints
- Write solutions in multiple languages (C++, Java, Python, JavaScript)
- Execute code against sample test cases
- Submit solutions to run against full hidden test case suites
- Receive real-time execution results & compilation logs
- View submission history and tracking
- View user dashboard with problem-solving statistics, streaks, and activity heatmaps

---

## ✨ Key Features

### 👤 User Management & User Dashboard
- JWT-based authentication & authorization
- User profile management
- Dashboard statistics (Easy, Medium, Hard solved counts)
- Real-time submission metrics & acceptance rates
- Streak tracking (current & max streak)
- Activity heatmap (daily submission calendar graph)

### 🧩 Problem Management
- Problem catalog with title, slug, difficulty, category, and acceptance rates
- Comprehensive problem descriptions, constraints, examples, and hints
- Multi-language starter code templates (`javascript`, `python`, `cpp`, `java`)
- Execution driver templates & canonical reference solutions
- Sample & hidden test case management
- Dynamic search and filter endpoints

### 💻 Containerized Online Code Execution Engine
- Ephemeral Docker container sandboxing with strict timeouts (2-min safety watchdog)
- Execution providers for C++, Java, Python, and JavaScript
- STDIN / STDOUT stream serialization & bitmask type parsing (`CodehornTestcaseParserStrategy`)

### 📊 Submission Management
- Submission lifecycle tracking
- Execution statuses (`ACCEPTED`, `WRONG_ANSWER`, `TIME_LIMIT_EXCEEDED`, `RUNTIME_ERROR`, `COMPILATION_ERROR`)
- Detailed runtime (ms) and memory usage metrics

---

## 🧱 Microservices Architecture

| Service | Responsibility | Technology |
| :--- | :--- | :--- |
| **Gateway Service** | Service routing, strip-prefix filtering, load balancing | Spring Cloud Gateway, Consul |
| **Auth Service** | Authentication & JWT token issuing | Spring Boot, Kotlin |
| **User Service** | Reactive user profiles, dashboards, stats, streaks, heatmaps | Spring WebFlux, Kotlin Coroutines, MongoDB |
| **Problems Service** | Problem metadata, starter templates, canonical solutions, testcases | Spring Boot, Spring Data MongoDB |
| **Problem Submission Service** | Submission lifecycle & submission tracking | Spring Boot |
| **Code Execution Service** | Central code execution orchestrator | Spring Boot, Kotlin Coroutines |
| **Language Execution Services** | Docker sandboxed execution runners (`cpp`, `java`, `python`, `javascript`) | Spring Boot, Docker sidecars (DinD) |
| **Frontend** | Interactive client web application | Next.js, React, TypeScript, Monaco Editor |

---

## 🛠️ Technology Stack

### Backend
- **Languages**: Kotlin, Java
- **Frameworks**: Spring Boot 3/4, Spring WebFlux, Spring Cloud Gateway, Spring Cloud Consul
- **Persistence**: MongoDB, Spring Data MongoDB
- **Execution Sandbox**: Docker, Ephemeral Containers, Bash Entrypoints
- **Build System**: Gradle multi-project build

### Frontend
- **Framework**: Next.js, React, TypeScript
- **Styling**: Vanilla CSS, Modern Glassmorphism & Responsive Layouts
- **Editor**: Monaco Editor

### Infrastructure & Cloud Deployment
- **Containerization**: Docker, Docker Compose
- **Orchestration**: Kubernetes, Helm
- **GitOps & CI/CD**: ArgoCD, GitHub Actions
- **Infrastructure as Code**: Terraform, GCP (GKE, VPC, Cloud NAT)
- **Ingress Gateway**: Nginx TCP Stream Proxy

---

## 🔐 Security Architecture

1. **JWT Authentication**: Secured user tokens for cross-service authentication.
2. **Code Execution Isolation**:
   ```plaintext
                Untrusted User Code
                        │
                        ▼
                Execution Service
                        │
                        ▼
             ┌─────────────────────┐
             │ Ephemeral Container │
             │                     │
             │ CPU & Memory Limits │
             │ Strict watchdog     │
             │ Isolated workspace  │
             └─────────────────────┘
                        │
                        ▼
                Parsed Test Results
   ```

---

## 🔄 Submission Workflow

```plaintext
1. User submits solution code from Editor
        ↓
2. Frontend sends request to API Gateway (/problem-submission)
        ↓
3. Gateway routes request to Problem Submission Service
        ↓
4. Execution Service triggers language runner (e.g., Java Execution Service)
        ↓
5. Input generator creates workspace & compiles code
        ↓
6. Container executes entrypoint.sh against testcase input streams
        ↓
7. Output parser evaluates STDOUT/STDERR vs expected testcase results
        ↓
8. Container and temporary images are purged
        ↓
9. User Service receives activity update (recalculates stats, streak & heatmap)
        ↓
10. Final submission status returned to Client
```

---

## 🗺️ System Roadmap

- [x] User Authentication & Authorization
- [x] Problem Management & CRUD APIs
- [x] Multi-Language Starter Code & Execution Driver Templates
- [x] Reactive User Service & Dashboard Statistics (Streaks, Heatmap)
- [x] Containerized Code Execution Engine (C++, Java, Python, JavaScript)
- [x] Service Discovery via Spring Cloud Consul
- [x] GKE Infrastructure & Terraform Provisioning
- [x] Helm Chart & ArgoCD GitOps Deployment
- [ ] Global Leaderboard System
- [ ] Real-time Live Contest Platform
- [ ] Distributed Tracing with OpenTelemetry & Grafana
