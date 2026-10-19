# 🛒 Modern E-Commerce RESTful API

A robust, enterprise-grade **E-Commerce Backend RESTful API** built with **Spring Boot**, **Spring Security**, **JWT (JSON Web Token)**, **Spring Data JPA**, and **MySQL**. 

This system provides end-to-end commerce features including role-based authentication, product catalogs with multi-level category hierarchies, shopping cart operations, order lifecycle management, stock control, procurement/buying tracking, and binary image storage.

---

## 📑 Table of Contents

- [Features](#-features)
- [Architecture & Tech Stack](#-architecture--tech-stack)
- [Project Structure](#-project-structure)
- [Preloaded Seed Data](#-preloaded-seed-data)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Database Setup](#database-setup)
  - [Configuration](#configuration)
  - [Running the Application](#running-the-application)
- [API Documentation](#-api-documentation)
  - [Authentication & Users](#1-authentication--users)
  - [Categories & Products](#2-categories--products)
  - [Images](#3-product-images)
  - [Cart & Checkout](#4-cart)
  - [Orders & Lifecycle](#5-orders)
  - [Stock & Inventory](#6-stock--inventory)
  - [Procurement / Buying](#7-procurement--buying)
  - [Customer & Addresses](#8-customer-profiles--addresses)
  - [List of Values (LOV) & Roles](#9-list-of-values-lov--roles)
- [Request & Response Samples](#-request--response-samples)
- [Exception Handling](#-exception-handling)

---

## ✨ Features

- **🔐 Authentication & Role-Based Access Control (RBAC)**
  - Stateless authentication powered by Spring Security & JJWT.
  - Granular role permission management (`ADMIN` vs `CUSTOMER`).
  - Secure token blacklisting on `/api/v1/auth/logout`.
  - Automatic seed initialization of default roles and administrator accounts.

- **📦 Product & Category Hierarchy**
  - Main categories and multi-level subcategories with parent-child relationships.
  - Products linked with categories, descriptions, brands, and price history tracking.
  - Multi-image support per product with direct BLOB storage and download streams.

- **🛒 Smart Shopping Cart**
  - Automatic per-customer persistent shopping cart.
  - Real-time cart total calculation.
  - Add, adjust item quantities, remove items, or clear cart.

- **📋 Order Lifecycle & Workflow Engine**
  - Instant conversion of cart items into orders.
  - State machine workflow: `PENDING` ➔ `PROCESSING` ➔ `SHIPPED` ➔ `DELIVERED`.
  - Built-in status advance (`next-status`), rollback (`roll-back-status`), and cancellation (`cancel`).
  - Dynamic List of Values (LOV) tracking for state management.

- **📊 Stock & Inventory Control**
  - Live inventory tracking per product.
  - Atomic stock deductions and rollback validations to prevent overselling (`InsufficientStockException`).
  - Direct inventory adjustment and set capabilities for store administrators.

- **🚚 Procurement & Buying Management**
  - Track supplier wholesale purchases and buying items.
  - Record batch quantities and cost prices.

- **🏠 Customer Address Book**
  - Support for multiple shipping and billing addresses per customer profile.

- **🛡️ Clean Code & Resilient Architecture**
  - Standardized JSON responses via `ApiResponse<T>`.
  - Comprehensive `@RestControllerAdvice` global exception handling.
  - DTO mapping isolation with ModelMapper.

---

## 🛠 Architecture & Tech Stack

| Component | Technology | Description |
|---|---|---|
| **Language** | Java 21 / 25 | Latest enterprise Java runtime features |
| **Framework** | Spring Boot | Core microservice framework (Spring Web MVC) |
| **Security** | Spring Security 6 & JJWT 0.11.5 | Stateless JWT authentication, role guards & token blacklist |
| **Persistence** | Spring Data JPA (Hibernate) | ORM database mapping and transactional queries |
| **Database** | MySQL 8.x | Relational database storage |
| **Data Mapping** | ModelMapper 3.2.0 | Separation between JPA Entities and DTO layer |
| **Validation** | Jakarta Bean Validation | Request payload validation (`@Valid`, `@NotNull`, etc.) |
| **Boilerplate** | Project Lombok | Getters, setters, builders, and constructors |
| **Build Tool** | Apache Maven | Dependency management and build packaging |

---

## 📂 Project Structure

```text
backend
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── emin
│   │   │           └── e_commerce
│   │   │               ├── ECommerceApplication.java   # Application entry point
│   │   │               ├── config/                     # ModelMapper & app configs
│   │   │               ├── controller/                 # REST API Controllers
│   │   │               ├── data/                       # Initial data loader (DataInitializer)
│   │   │               ├── dto/                        # Data Transfer Objects
│   │   │               ├── exception/                  # Global exception handlers
│   │   │               ├── model/                      # JPA Entities
│   │   │               ├── repository/                 # Spring Data JPA Repositories
│   │   │               ├── request/                    # HTTP Request payload models
│   │   │               ├── response/                   # Standardized ApiResponse & DTOs
│   │   │               ├── security/                   # JWT filters, entry points, & SecurityConfig
│   │   │               ├── service/                    # Business service interfaces & implementations
│   │   │               └── util/                       # Password encoders and helper utilities
│   │   └── resources
│   │       └── application.properties                  # Database & application properties
│   └── test                                            # Integration and unit tests
```

---

## ⚡ Preloaded Seed Data

When the application boots for the first time, `DataInitializer` automatically ensures the following records are seeded:

### Default Admin Account
- **Email:** `admin@email.com`
- **Password:** `123456`
- **Role:** `ADMIN`

### Default Roles
- `ROLE_ADMIN`
- `ROLE_CUSTOMER`

### Order Status Flow (List of Values)
- `PENDING`
- `PROCESSING`
- `SHIPPED`
- `DELIVERED`
- `CANCELLED`

---

## 🚀 Getting Started

### Prerequisites

- **Java JDK 21+** or **25**
- **Maven 3.8+** (or use the included `./mvnw`)
- **MySQL 8.0+**

### Database Setup

Create a new MySQL database:

```sql
CREATE DATABASE ecommerce_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Configuration

Open `src/main/resources/application.properties` and update your database credentials and secret key:

```properties
server.port=9191

# MySQL Configuration
spring.datasource.url=your_datasource_url
spring.datasource.username=your_user_name
spring.datasource.password=your_mysql_password

# Hibernate Settings
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=update

# Multipart File Limits
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB

# JWT Security Settings
auth.token.jwtSecret=your_jwt_secret
auth.token.expirationInMils=86400000
```

### Running the Application

1. Build and run using the Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

2. The API server will start listening at:
   ```text
   http://localhost:9191
   ```

---

## 📖 API Documentation

All protected endpoints require the HTTP Header:
```http
Authorization: Bearer <your_jwt_token>
```

### 1. Authentication & Users

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | Public | Authenticate user & receive JWT token |
| `POST` | `/api/v1/auth/logout` | Authenticated | Invalidate JWT token (blacklist) |
| `POST` | `/api/v1/customers/add` | Public | Register a new customer |

### 2. Categories & Products

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/categories/all` | Public | Get all categories |
| `GET` | `/api/v1/categories/main` | Public | Get root / top-level categories |
| `GET` | `/api/v1/categories/parent/{parentId}` | Public | Get subcategories by parent ID |
| `GET` | `/api/v1/categories/category/{id}` | Public | Get category details |
| `POST` | `/api/v1/categories/add` | `ADMIN` | Create a new category |
| `PUT` | `/api/v1/categories/category/{id}/update` | `ADMIN` | Update category details |
| `DELETE` | `/api/v1/categories/category/{id}/delete` | `ADMIN` | Remove category |
| `GET` | `/api/v1/products/all` | Public | Get all products |
| `GET` | `/api/v1/products/product/{id}` | Public | Get product by ID |
| `POST` | `/api/v1/products/add` | `ADMIN` | Create a new product |
| `PUT` | `/api/v1/products/product/{id}/update` | `ADMIN` | Update product details |
| `DELETE` | `/api/v1/products/product/{id}/delete` | `ADMIN` | Delete product |

### 3. Product Images

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/images/image/download/{imageId}` | Public | Stream / download product image |
| `POST` | `/api/v1/images/upload` | `ADMIN` | Upload one or multiple images for a product (`multipart/form-data`) |
| `PUT` | `/api/v1/images/image/{imageId}/update` | `ADMIN` | Update image binary |
| `DELETE` | `/api/v1/images/image/{imageId}/delete` | `ADMIN` | Delete an image |

### 4. Cart

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/carts/my-cart` | `CUSTOMER`, `ADMIN` | Get current customer's cart |
| `POST` | `/api/v1/carts/add` | `CUSTOMER`, `ADMIN` | Add item to cart (`?productId=1&quantity=2`) |
| `PUT` | `/api/v1/carts/update` | `CUSTOMER`, `ADMIN` | Update item quantity (`?productId=1&quantity=3`) |
| `DELETE` | `/api/v1/carts/remove/product` | `CUSTOMER`, `ADMIN` | Remove item from cart (`?productId=1`) |
| `DELETE` | `/api/v1/carts/clear` | `CUSTOMER`, `ADMIN` | Empty cart |

### 5. Orders

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/v1/orders/add` | `CUSTOMER`, `ADMIN` | Checkout: create order from current cart |
| `GET` | `/api/v1/orders/my-orders` | `CUSTOMER`, `ADMIN` | List orders for current authenticated user |
| `GET` | `/api/v1/orders/order/{id}` | `CUSTOMER`, `ADMIN` | Get order details |
| `GET` | `/api/v1/orders/all` | `ADMIN` | List all system orders |
| `PUT` | `/api/v1/orders/order/{id}/update` | `ADMIN` | Update order and reset to `PENDING` |
| `PUT` | `/api/v1/orders/order/{id}/next-status` | `ADMIN` | Transition status forward (`PENDING` ➔ `PROCESSING` ➔ ...) |
| `PUT` | `/api/v1/orders/order/{id}/roll-back-status` | `ADMIN` | Rollback status to previous state |
| `PUT` | `/api/v1/orders/order/{id}/cancel` | `ADMIN` | Cancel order |
| `DELETE` | `/api/v1/orders/order/{id}/delete` | `ADMIN` | Delete order |

### 6. Stock & Inventory

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/stocks/product/{productId}` | `ADMIN` | Get inventory stock level for product |
| `POST` | `/api/v1/stocks/product/{productId}/update` | `ADMIN` | Increment or decrement stock (`?quantityChange=5`) |
| `POST` | `/api/v1/stocks/product/{productId}/set` | `ADMIN` | Set exact stock amount (`?quantity=100`) |

### 7. Procurement / Buying

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/v1/buyings/add` | `ADMIN` | Record supplier wholesale purchase |
| `GET` | `/api/v1/buyings/all` | `ADMIN` | List all procurement records |
| `GET` | `/api/v1/buyings/buying/{id}` | `ADMIN` | Get procurement batch details |
| `DELETE` | `/api/v1/buyings/buying/{id}/delete` | `ADMIN` | Delete procurement record |

### 8. Customer Profiles & Addresses

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/customers/customer/me` | `CUSTOMER`, `ADMIN` | View own customer profile |
| `GET` | `/api/v1/customers/all` | `ADMIN` | List all registered customers |
| `GET` | `/api/v1/customers/customer/{id}` | `ADMIN` | Get customer by ID |
| `PUT` | `/api/v1/customers/customer/{id}/update` | `CUSTOMER`, `ADMIN` | Update customer profile |
| `DELETE` | `/api/v1/customers/customer/{id}/delete` | `ADMIN` | Remove customer account |
| `GET` | `/api/v1/addresses/my-addresses` | `CUSTOMER`, `ADMIN` | List current customer's addresses |
| `POST` | `/api/v1/addresses/add` | `CUSTOMER`, `ADMIN` | Add shipping / billing address |
| `PUT` | `/api/v1/addresses/address/{id}/update` | `CUSTOMER`, `ADMIN` | Update address |
| `DELETE` | `/api/v1/addresses/address/{id}/delete` | `CUSTOMER`, `ADMIN` | Delete address |

### 9. List of Values (LOV) & Roles

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/list-of-values/all` | `ADMIN` | Get all system LOV entries |
| `GET` | `/api/v1/list-of-values/type/{type}` | `ADMIN` | Filter LOV by type (e.g. `ORDER_STATUS`) |
| `POST` | `/api/v1/list-of-values/add` | `ADMIN` | Create new LOV entry |
| `GET` | `/api/v1/roles/all` | `ADMIN` | View all system roles |
| `POST` | `/api/v1/roles/add` | `ADMIN` | Create custom role |

---

## 💡 Request & Response Samples

### 1. User Registration (`POST /api/v1/customers/add`)
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "Password123!",
  "phone": "+1234567890"
}
```

### 2. User Login (`POST /api/v1/auth/login`)
```json
{
  "email": "admin@email.com",
  "password": "123456"
}
```
**Response:**
```json
{
  "message": "Login successful!",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsIn...",
    "email": "admin@email.com",
    "role": "ADMIN"
  }
}
```

### 3. Add Item to Cart (`POST /api/v1/carts/add?productId=1&quantity=2`)
**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "message": "Product added to cart successfully!",
  "data": {
    "id": 1,
    "totalAmount": 199.98,
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Wireless Noise Cancelling Headphones",
        "quantity": 2,
        "unitPrice": 99.99,
        "totalPrice": 199.98
      }
    ]
  }
}
```

### 4. Create Order (`POST /api/v1/orders/add`)
**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "message": "Order placed successfully!",
  "data": {
    "id": 101,
    "orderDate": "2026-10-18T18:00:00",
    "totalAmount": 199.98,
    "orderStatus": "PENDING",
    "items": [
      {
        "productId": 1,
        "productName": "Wireless Noise Cancelling Headphones",
        "quantity": 2,
        "price": 99.99
      }
    ]
  }
}
```

---

## 🚨 Exception Handling

Every response adheres to a predictable structure:

```json
{
  "message": "Error description or validation message",
  "data": null
}
```

| HTTP Code | Exception Class | Description |
|---|---|---|
| `400 BAD REQUEST` | `MethodArgumentNotValidException` | Input validation constraint violation |
| `400 BAD REQUEST` | `InsufficientStockException` | Requested quantity exceeds available inventory |
| `401 UNAUTHORIZED` | `AuthenticationException` | Invalid email/password credentials or expired token |
| `403 FORBIDDEN` | `AccessDeniedException` | Insufficient permissions for endpoint |
| `404 NOT FOUND` | `NotFoundException` | Resource (Product, Category, Order, User) not found |
| `409 CONFLICT` | `AlreadyExistsException` | Email or unique field duplicate conflict |
| `500 INTERNAL` | `Exception` | Unhandled server error |
