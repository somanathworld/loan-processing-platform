System Architecture
====================
                   ┌────────────────────────────┐
                   │     API Gateway (Zuul / Spring Cloud Gateway)     │
                   └────────────────────────────┘
                                 │
         ┌────────────┬──────────────┬────────────┐
         ▼            ▼              ▼            ▼
┌────────────────┐ ┌────────────────┐ ┌────────────────┐ ┌────────────────────┐
│ Auth Service   │ │ User Service   │ │ Loan Service   │ │ Credit Score Svc   │
└────────────────┘ └────────────────┘ └────────────────┘ └────────────────────┘
       │                    │                     │                      │
       ▼                    ▼                     ▼                      ▼
 PostgreSQL         MongoDB             MySQL                 Redis / Kafka

Service-Wise breakdown
======================
| Service                       | Description                             | Tech Stack                                          |
| ----------------------------- | --------------------------------------- | --------------------------------------------------- |
| **API Gateway**               | Entry point, routing, load balancing    | Spring Cloud Gateway                                |
| **Auth Service**              | JWT-based login, token refresh          | Spring Boot + Spring Security + Keycloak (optional) |
| **User Service**              | Register customers, update KYC          | Spring Boot + MongoDB                               |
| **Loan Service**              | Submit loan applications, track status  | Spring Boot + MySQL + JPA                           |
| **Credit Score Service**      | Calculates score based on user history  | Spring Boot + Redis (cache) + Kafka for events      |
| **Notification Service**      | Sends email/SMS for approval, rejection | Spring Boot + Mail + Kafka consumer                 |
| **Config Server (optional)**  | Centralized config management           | Spring Cloud Config                                 |
| **Discovery Server (Eureka)** | Register & discover services            | Spring Cloud Netflix Eureka                         |
| **API Documentation**         | Swagger UI for each service             | SpringDoc / Swagger 3                               |

Data Flow Example (Loan Application)
===================================
User signs up via User Service.
Auth Service issues JWT.
User logs in and submits a loan request via Loan Service.
Loan Service fires event to Kafka.
Credit Score Service processes event, updates score.
Score sent back to Loan Service → triggers workflow.
Loan is approved/rejected and notification is sent.
Audit logs stored and accessible by admins.

Folder Structure (Simplified)
=============================
loan-platform/
├── api-gateway/
├── auth-service/
├── user-service/
├── loan-service/
├── credit-score-service/
├── notification-service/
├── discovery-server/
├── config-server/ (optional)
├── docker-compose.yml
├── README.md
└── jenkins-pipeline/

Phases
=======
🔹 Phase 1: Project Setup & Core Services
Set up Eureka Discovery Server
Set up API Gateway using Spring Cloud Gateway
Create base structure for:

auth-service
user-service
loan-service

📦 Add dependencies: spring-boot-starter-web, spring-security, spring-data-jpa, spring-cloud-starter-netflix-eureka-client

🔹 Phase 2: Auth Service with JWT
Create user login/signup endpoint
Implement UserDetailsService, JWTFilter, TokenProvider
Add JWT Token in headers from gateway → other services

🔐 Use:

Authorization: Bearer <JWT-TOKEN>

🔹 Phase 3: User + Loan Service Development
User Service:
Register customer
Upload KYC
Save in MongoDB

Loan Service:
Apply for a loan
Assign loan ID
Publish to Kafka

🔹 Phase 4: Kafka Integration + Credit Score Logic
Add Kafka dependencies in pom.xml
Set up Kafka broker in Docker Compose
Credit Score Service listens to loan topics
Apply logic like:

if (loanAmount < 500000) return "Good";
else return "Moderate";
Store score in Redis cache with expiry

🔹 Phase 5: Notification + Docs + Testing
Notification Service listens to approval/rejection Kafka topics
Sends mock email via MailTrap or logs it
Add Swagger UI to all services
Test APIs using Postman

🔹 Phase 6: Dockerize + CI/CD Pipeline
Create Dockerfile for each service
Use docker-compose.yml to:
Spin all microservices
Spin Kafka, Redis, MySQL, MongoDB
Setup GitHub Actions / Jenkins:
mvn clean package
docker build
docker push (optional)
docker-compose up -d

🔹 Phase 7: Inter-Service Communication
Real World Use-Case in Your Project:
Your loan-service may need to:
Fetch user details from user-service
Send loan approval status to notification-service

🔹 Phase 8: Distributed Logging & Tracing
✅ Focus: Observe and trace all microservice interactions.

🔹 Phase 9: Resilience & Circuit Breaker
✅ Focus: Prevent cascading failures in case one service is down.

1. Parent Folder creation - Loan-processing-platform ✅ 
2. Child folder creation - auth-service, user-service, discovery-servcie....
3. Create a Spring boot project in all child services folder
4. Create demo template for all the child sevice : rest endpoints to check whther request are successfully parsed.
5. Create Dockerfile  and docker-compose file in parent folder for checking all child services for integrating with each other.
6. Perform coding as per the phases as mentioned above

Kubernetes start
----------------
minikube start

eval $(minikube docker-env)

docker build -t discovery-server:1.0 ./discovery-server

docker build -t loan-service:1.0 ./loan-service

docker build -t auth-service:1.0 ./auth-service

docker build -t user-service:1.0 ./user-service

docker build -t api-gateway:1.0 ./api-gateway

kubectl apply -f discovery-deployment.yaml

kubectl apply -f discover-service.yaml

kubectl apply -f loan-deployment.yaml

kubectl apply -f loan-service.yaml

kubectl apply -f auth-deployment.yaml

kubectl apply -f auth-service.yaml

kubectl apply -f user-deployment.yaml

kubectl apply -f user-service.yaml

kubectl apply -f zipkin-deployment.yaml

kubectl apply -f zipkin-service.yaml

kubectl apply -f zookeeper-deployment.yaml

kubectl apply -f zookeeper-service.yaml

kubectl apply -f kafka-deployment.yaml

kubectl apply -f kafka-service.yaml

kubectl get pods

minikube service api-gateway --url