# Webshop Project

Small project utilizing the following techonogies:
- Spring Boot Microservices
- MongoDB (Product Service)
- MySQL (Inventory + Order Services)
- Testcontainers for Integration tests
- Spring Cloud Netflix Eureka

    - Client-side service discovery allows services to find and communicate with each other without hard-coding the hostname and port.
    - Multiple instances of Inventory Service.
- Keycloak for Identity And Access Management
- Resilience4j: Circuit  Breaker, Timelimiter, Retry  
- Zipkin for distributed tracing system

## Modules

- Product Service
- Order Service
- Inventory Service
- Discovery Server

    - Utilizes Netflix Eureka. 