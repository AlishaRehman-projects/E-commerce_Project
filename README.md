# E-Commerce Backend API

A fully functional e-commerce REST API built with Spring Boot, featuring JWT authentication,
role-based access control, cart management, order processing, and payment integration.

## Tech Stack

- **Java 17** — Core language
- **Spring Boot** — Application framework
- **Spring Security** — Authentication & authorization
- **Spring Data JPA** — Database ORM
- **MySQL** — Relational database
- **JWT (jjwt)** — Stateless authentication
- **Razorpay** — Payment gateway

## Features

- JWT-based authentication with email and password
- Role-based access control (USER / ADMIN)
- Product management with image upload and keyword search
- Cart management (add, remove, update quantity, clear)
- Order management with price snapshot on checkout
- Razorpay payment integration with HMAC-SHA256 signature verification

## Getting Started

### Prerequisites
- Java 17+
- MySQL
- Maven

### Setup

1. **Clone the repository**
```bash
   git clone https://github.com/yourusername/ecom-backend.git
   cd ecom-backend
```

2. **Create MySQL database**
```sql
   CREATE DATABASE ecom_db;
```

3. **Configure `application.properties`**
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecom_db
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   spring.jpa.hibernate.ddl-auto=update

   jwt.secret=your_base64_encoded_secret

   razorpay.key.id=rzp_test_your_key_id
   razorpay.key.secret=your_key_secret
```

4. **Run the application**
```bash
   mvn spring-boot:run
```

The server starts at `http://localhost:8080`

## API Reference

### Auth
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/register` | Public | Register a new user |
| POST | `/login` | Public | Login and get JWT token |

### Products
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/products` | Public | Get all products |
| GET | `/api/product/{id}` | Public | Get product by ID |
| GET | `/api/product/{id}/image` | Public | Get product image |
| GET | `/api/products/search?keyword=` | Public | Search products |
| POST | `/api/product` | ADMIN | Add new product |
| PUT | `/api/product/{id}` | ADMIN | Update product |
| DELETE | `/api/product/{id}` | ADMIN | Delete product |

### Cart
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/cart/` | USER | View cart |
| POST | `/api/cart/add/{productId}?quantity=` | USER | Add item to cart |
| PUT | `/api/cart/update/{cartItemId}?quantity=` | USER | Update item quantity |
| DELETE | `/api/cart/remove/{cartItemId}` | USER | Remove item from cart |
| DELETE | `/api/cart/clear` | USER | Clear entire cart |

### Orders
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/orders/checkout` | USER | Place order from cart |
| GET | `/api/orders` | USER | Get order history |
| GET | `/api/orders/{orderId}` | USER | Get order by ID |
| PUT | `/api/orders/{orderId}/status?status=` | ADMIN | Update order status |

### Payment
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/payment/create/{orderId}` | USER | Create Razorpay order |
| POST | `/api/payment/verify/{orderId}` | USER | Verify payment signature |
Authorization: Bearer <your_jwt_token>

Get the token by calling `/login` with your email and password.

## Order Status Flow
PENDING → PAID → SHIPPED → DELIVERED
↘ CANCELLED

## Project Structure
src/main/java/com/example/ecom_proj/
├── config/ # Security config, JWT filter
├── controller/ # REST controllers
├── model/ # JPA entities
├── repo/ # Spring Data repositories
└── service/ # Business logic

## Authentication

All protected endpoints require a Bearer token in the request header:
