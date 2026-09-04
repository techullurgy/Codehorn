# CodeHorn

**CodeHorn** is a cloud-native online coding judge platform inspired by LeetCode, designed for developers to solve programming challenges, execute code securely, and receive instant feedback. The platform combines a modern web experience with a scalable microservices backend capable of handling concurrent code execution, automated evaluation, and distributed workloads.

Built with a strong emphasis on scalability, security, and DevOps, CodeHorn demonstrates the architecture required to operate a production-ready coding platform—from responsive frontend applications to containerized code execution and Kubernetes-based infrastructure.

---

# Features

## Coding Challenges

- Browse programming problems across multiple difficulty levels.
- Rich problem descriptions with examples and constraints.
- Support for multiple programming languages.
- Real-time code editing and submission.
- Instant evaluation against predefined test cases.
- Submission history and execution results.

---

## Online Code Execution

The platform securely executes user-submitted code inside isolated containers.

Features include:

- Sandboxed execution environment.
- Resource isolation using Docker.
- Time and memory limit enforcement.
- Compilation and runtime error reporting.
- Test case validation.
- Execution logs and performance metrics.

---

## Responsive Web Experience

Built with modern frontend technologies to provide a fast and intuitive user experience.

Features include:

- Responsive layouts for desktop and mobile.
- Fast client-side navigation.
- Efficient data fetching and caching.
- Modern, accessible user interface.

---

# Frontend Architecture

The frontend is built using the latest React ecosystem, focusing on performance, maintainability, and developer experience.

## Core Technologies

### Next.js

- Server-side rendering (SSR).
- Static site generation (SSG).
- Optimized routing.
- Excellent SEO support.

### React

- Component-based architecture.
- Reusable UI components.
- Declarative rendering.
- State-driven user interface.

### TypeScript

- Strong type safety.
- Improved maintainability.
- Better developer tooling and IntelliSense.

### TanStack Query

- Efficient server-state management.
- Intelligent caching.
- Background data synchronization.
- Automatic request retries and cache invalidation.

### Tailwind CSS

- Utility-first styling.
- Responsive design.
- Consistent design system.
- Rapid UI development.

---

# Backend Architecture

The backend follows a distributed **microservices architecture**, enabling independent scaling and deployment of each service.

## Core Technologies

### Kotlin + Spring Boot

- REST APIs.
- Authentication and authorization.
- Problem management.
- Submission processing.
- User management.

### Microservices

Independent services communicate through well-defined APIs and asynchronous workflows, allowing the platform to scale efficiently as demand grows.

### Service Discovery

- Consul for dynamic service registration and discovery.
- Automatic health checks and service availability monitoring.
- Service-to-service communication without hardcoded endpoints.

---

## Secure Code Execution

One of the platform's core components is the isolated execution environment for user submissions.

Key capabilities include:

- Docker-in-Docker (DinD) for sandboxed code execution.
- Isolated runtime environments.
- Secure compilation and execution.
- Resource limits to prevent abuse.
- Support for multiple programming languages.

---

# Cloud-Native Infrastructure

The platform is designed for modern cloud environments with automation, scalability, and reliability in mind.

## Containerization & Orchestration

- Docker
- Kubernetes
- Helm Charts
- Helm Templates
- Consul Service Discovery

## Infrastructure as Code

- Terraform

## GitOps

- ArgoCD for automated deployments.
- Declarative infrastructure management.
- Environment-specific configuration.

---

# Monitoring & Observability

The platform incorporates comprehensive observability to monitor application health and performance.

Technologies include:

- OpenTelemetry
- Micrometer
- Grafana

Key metrics include:

- Request latency.
- API throughput.
- Resource utilization.
- Service health.
- Distributed tracing.
- Application metrics.

---

# CI/CD Pipeline

Automated deployment pipelines ensure reliable software delivery.

Features include:

- GitHub Actions.
- Automated builds.
- Unit and integration testing.
- Docker image generation.
- Kubernetes deployments.
- Continuous delivery using GitOps.

---

# Technical Focus

CodeHorn demonstrates the design and implementation of a production-ready cloud-native platform by combining modern frontend development with scalable backend infrastructure.

Key areas include:

- Online code execution.
- Secure containerized workloads.
- Distributed microservices architecture.
- Service discovery and health monitoring.
- Infrastructure as Code.
- Kubernetes orchestration.
- GitOps deployment workflows.
- Monitoring and observability.
- Modern React and Next.js development.
- API-driven architecture.
- High-performance, responsive user interfaces.

---

# Skills Demonstrated

## Frontend

- React
- Next.js
- TypeScript
- TanStack Query
- Tailwind CSS
- Responsive Web Design
- State Management
- API Integration

## Backend

- Kotlin
- Spring Boot
- Microservices Architecture
- REST API Development
- Service Discovery (Consul)
- Secure Code Execution
- Docker & Docker-in-Docker (DinD)

## Cloud & DevOps

- Docker
- Kubernetes
- Helm
- Terraform
- GitHub Actions
- GitOps (ArgoCD)
- OpenTelemetry
- Micrometer
- Grafana
- CI/CD Pipelines
- Cloud-Native Architecture
- Distributed Systems