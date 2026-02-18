# 🍽️ Restaurant Review API

A scalable RESTful backend application built using **Spring Boot**, **Elasticsearch**, and **Keycloak (OAuth2 Resource Server with JWT)**.

This system allows users to create restaurants, post reviews, upload photos, and perform advanced search operations including full-text and geo-location queries.

Designed with clean architecture principles and production-ready backend patterns.

---

## 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- OAuth2 Resource Server
- Keycloak (JWT Authentication)
- Elasticsearch
- Spring Data Elasticsearch
- MapStruct (DTO Mapping)
- Lombok (Builder Pattern)
- Kibana (Index inspection)

---

## 🏗 Architecture

The application follows a layered architecture:

Controller → Service → Repository → Elasticsearch

### Architectural Practices

- DTO + Mapper Pattern (MapStruct)
- Builder Pattern (Lombok)
- Global Exception Handling (@ControllerAdvice)
- Stateless Security Configuration
- Nested Document Modeling (Elasticsearch)
- Manual Pagination for Nested Reviews
- Ownership-based Access Control

---

## 🔐 Authentication & Security

- OAuth2 Resource Server configuration
- JWT validation via Keycloak
- Stateless session management
- User identity extracted from JWT
- No role-based access (all authenticated users)

### Public Endpoints

- `GET /api/v1/restaurants`
- `GET /api/v1/photos/**`

All other endpoints require authentication.

### Review Ownership Rules

- A user can review a restaurant only once
- Only the author can edit/delete their review
- Reviews can be edited within 48 hours only

---

## 🗄 Data Modeling (Elasticsearch)

### Index: `restaurants`

Restaurants are stored as top-level documents.

Reviews and Photos are stored as nested objects inside the Restaurant document.

### Why Nested Modeling?

- Fast single-document retrieval
- Aggregated data stored together
- Simplified consistency handling
- Efficient average rating calculation

---

## 🔎 Search Capabilities

### 1️⃣ Full-Text Fuzzy Search

Search by restaurant name or cuisine type.


## ⭐ Review Management

### Features

- Create Review
- Update Review (within 48 hours)
- Delete Review
- List Reviews (sorted & paginated)
- Automatic average rating recalculation

### Business Logic Enforcement

- Prevent duplicate reviews by same user
- Validate review ownership before update/delete
- Recalculate average rating after create/update/delete

---

## 📸 Photo Management

- Multipart file upload
- MediaType auto-detection
- Inline image rendering
- Stored as nested object

---

## 🛡 Global Exception Handling

Centralized error handling using `@ControllerAdvice`.

Handles:

- RestaurantNotFoundException
- ReviewNotFoundException
- ReviewNotAllowedException
- Validation errors
- StorageException
- Generic Exception fallback

Ensures consistent API responses.

---

## 📡 API Endpoints

### Restaurants

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/restaurants | Create restaurant |
| GET | /api/v1/restaurants | Search restaurants |
| GET | /api/v1/restaurants/{id} | Get restaurant by ID |
| PUT | /api/v1/restaurants/{id} | Update restaurant |
| DELETE | /api/v1/restaurants/{id} | Delete restaurant |

---

### Reviews

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/restaurants/{id}/reviews | Create review |
| GET | /api/v1/restaurants/{id}/reviews | List reviews |
| GET | /api/v1/restaurants/{id}/reviews/{reviewId} | Get review |
| PUT | /api/v1/restaurants/{id}/reviews/{reviewId} | Update review |
| DELETE | /api/v1/restaurants/{id}/reviews/{reviewId} | Delete review |

---

### Photos

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/photos | Upload photo |
| GET | /api/v1/photos/{id} | Retrieve photo |

---

## 💛 Show Your Support

If you liked this project or found it useful,  
a ⭐ on the repository would truly be appreciated.

Thank you for taking the time to explore my work!
