<h1 align="center">🛒 Retail Management Backend System</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" />
  <img src="https://img.shields.io/badge/Spring Boot-3.2.5-brightgreen?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql" />
  <img src="https://img.shields.io/badge/MongoDB-7.x-green?style=for-the-badge&logo=mongodb" />
  <img src="https://img.shields.io/badge/Maven-3.9-red?style=for-the-badge&logo=apachemaven" />
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge" />
</p>

<p align="center">
  A production-grade <strong>Retail Management REST API</strong> built with Java 17 and Spring Boot 3.<br/>
  Supports multi-store management, product catalog, inventory tracking, order processing,<br/>
  MongoDB-backed reviews, and SQL stored procedure analytics.
</p>

---

## 📌 Features

- 🏪 **Store Management** — Create and manage multiple retail store locations
- 📦 **Product Catalog** — Full CRUD for products with category-based search
- 🗄 **Inventory Control** — Per-store stock levels with real-time updates
- 🛒 **Order Processing** — Place orders with automatic stock deduction and customer management
- ⭐ **Product Reviews** — MongoDB-backed review system per product
- 📊 **Sales Analytics** — MySQL Stored Procedures for monthly/company/top-product reports
- 🛡 **Global Exception Handling** — Consistent JSON error responses
- 🌐 **CORS Enabled** — Ready to connect with any frontend

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Relational DB | MySQL 8.0 (Spring Data JPA / Hibernate) |
| NoSQL DB | MongoDB (Spring Data MongoDB) |
| Analytics | MySQL Stored Procedures via JdbcTemplate |
| Validation | Jakarta Validation (Hibernate Validator) |
| Boilerplate | Project Lombok |
| Build Tool | Apache Maven 3.9 |

---

## 📁 Project Structure

```
Retail Management/
├── pom.xml
├── requests.http                          # HTTP test file for all endpoints
├── retail-management-api.postman_collection.json
├── src/
│   ├── main/
│   │   ├── java/com/retail/management/
│   │   │   ├── RetailManagementApplication.java
│   │   │   ├── config/CorsConfig.java
│   │   │   ├── controller/               # REST Controllers (5)
│   │   │   ├── dto/                      # Data Transfer Objects (14)
│   │   │   ├── entity/                   # JPA + MongoDB Entities (7)
│   │   │   ├── exception/                # Custom Exceptions + GlobalExceptionHandler
│   │   │   ├── repository/               # JPA + MongoDB Repositories (7)
│   │   │   └── service/                  # Business Logic Services (4)
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/
│   │           ├── schema.sql            # MySQL table definitions
│   │           ├── data.sql              # Sample data (5 stores, 15 products, orders)
│   │           └── stored_procedures.sql # 3 analytics stored procedures
│   └── test/                             # Unit & Slice Tests (7 tests)
```

---

## 🗄 Database Setup

### MySQL

```sql
CREATE DATABASE retail_management;
```

Then run the SQL scripts in order:

```bash
mysql -u root -p retail_management < src/main/resources/db/schema.sql
mysql -u root -p retail_management < src/main/resources/db/data.sql
mysql -u root -p retail_management < src/main/resources/db/stored_procedures.sql
```

### MongoDB

Make sure MongoDB is running on `localhost:27017`. The `retail_reviews` database and `reviews` collection are created automatically on first use.

---

## ⚙️ Configuration

Set environment variables or update `src/main/resources/application.properties`:

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `localhost` | MySQL host |
| `DB_PORT` | `3306` | MySQL port |
| `DB_NAME` | `retail_management` | MySQL database name |
| `DB_USER` | `root` | MySQL username |
| `DB_PASSWORD` | *(required)* | MySQL password |
| `MONGO_URI` | `mongodb://localhost:27017/retail_reviews` | MongoDB connection URI |
| `PORT` | `8080` | Server port |

---

## 🚀 Running the Application

```bash
# Build
mvn clean package -DskipTests

# Run
mvn spring-boot:run
```

Server starts at: **`http://localhost:8080`**

---

## 📡 API Endpoints

### Stores
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/stores` | Create a new store |
| `GET` | `/api/stores` | Get all stores |
| `GET` | `/api/stores/{id}` | Get store by ID |

### Products
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/products` | Create a product |
| `GET` | `/api/products` | Get all products |
| `GET` | `/api/products/{id}` | Get product by ID |
| `PUT` | `/api/products/{id}` | Update product |
| `DELETE` | `/api/products/{id}` | Delete product |
| `GET` | `/api/products/search?name=&category=` | Search products |

### Inventory
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/inventory/store/{storeId}` | Get inventory for a store |
| `GET` | `/api/inventory/store/{storeId}/search?name=&category=` | Search store inventory |
| `POST` | `/api/inventory` | Add inventory record |
| `PUT` | `/api/inventory/store/{storeId}/product/{productId}` | Update stock level |
| `DELETE` | `/api/inventory/store/{storeId}/product/{productId}` | Remove inventory record |

### Orders
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/orders/validate/{storeId}` | Validate store exists |
| `POST` | `/api/orders` | Place an order |

#### Place Order — Sample Request Body
```json
{
  "storeId": 1,
  "customer": {
    "name": "John Smith",
    "email": "john@example.com",
    "phone": "+91-9000000001"
  },
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 13, "quantity": 1 }
  ]
}
```

### Reviews (MongoDB)
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/reviews` | Submit a product review |
| `GET` | `/api/reviews/product/{productId}` | Get reviews for a product |

### Reports (Stored Procedures)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/reports/monthly-sales?year=&month=` | Sales per store for a month |
| `GET` | `/api/reports/company-sales?year=&month=` | Total company sales for a month |
| `GET` | `/api/reports/top-products?month=&year=` | Top selling product per category |

---

## 🛡 Error Response Format

All errors return a consistent JSON body:

```json
{
  "timestamp": "2026-11-02T18:52:12",
  "status": 404,
  "error": "Not Found",
  "message": "Store not found with id: 99",
  "path": "/api/stores/99"
}
```

---

## 🧪 Running Tests

```bash
mvn test
```

7 tests covering:
- `ServiceClassTest` — Store, Product, Inventory unit tests
- `OrderServiceTest` — Order placement and insufficient stock tests
- `StoreControllerTest` — Controller slice test with MockMvc

---

## 📬 Postman Collection

Import `retail-management-api.postman_collection.json` into Postman to test all endpoints instantly.

---

## 📜 License

This project is licensed under the MIT License.

---

<p align="center">Built with ❤️ using Java 17 + Spring Boot 3</p>
