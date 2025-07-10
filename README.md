
# 📊 JSON GroupBy and SortBy Backend Application

A Spring Boot application that allows you to **insert JSON records into named datasets** and query them using dynamic **group-by** and **sort-by** operations. Useful for generic data storage and querying use cases like dashboards, analytics, or ETL pipelines.

---

## 🚀 Features

- ✅ Insert any JSON payload dynamically into datasets.
- ✅ Group records by any JSON field (`GET /query?groupBy=field`).
- ✅ Sort records by any JSON field (`GET /query?sortBy=field`).
- ✅ Robust error handling and validation.
- ✅ Screenshot and DB output included in `/static`.

---

## 🛠 Tech Stack

| Layer        | Technology                          |
|--------------|--------------------------------------|
| Backend      | Java 21 + Spring Boot 3.5.3          |
| Persistence  | MySQL + Spring Data JPA              |
| JSON Parsing | Jackson (ObjectMapper)               |
| Testing      | JUnit 5 + Mockito                    |
| Mapping      | ModelMapper                          |

---

## 📁 Project Structure

```

JsonGroubByApplication/
├── src/
│   ├── main/
│   │   ├── java/com/example/JsonGroubByApplication/
│   │   │   ├── controller/         # REST API Controller
│   │   │   ├── service/            # Service Layer + Interface
│   │   │   ├── dto/                # DTOs for request/response
│   │   │   ├── entity/             # JPA Entity
│   │   │   ├── repository/         # Spring Data Repository
│   │   │   ├── exception/          # Global Error Handlers
│   │   │   └── config/             # Mapper Configs
│   ├── resources/
│   │   ├── application.properties
│   │   └── static/outputs/         # Screenshots & sample JSON
└── pom.xml

````

---

## 🧑‍💻 How to Run

### ✅ Prerequisites

- Java 21+
- Maven 3.8+
- MySQL running locally (or remotely)

### ✅ Clone and Run

```bash
git clone https://github.com/your-username/json-groupby-app.git
cd json-groupby-app
````

Update your MySQL credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jsongroupby
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

Then run the app:

```bash
mvn clean spring-boot:run
```

---

## 📬 API Endpoints

Base URL: `http://localhost:8080/api/dataset/{datasetName}`

---

### ➕ Insert Record

```http
POST /api/dataset/employees/record
```

**Request Body**:

```json
{
  "id": 3,
  "name": "Alice",
  "department": "Engineering",
  "age": 29
}
```

**Response**:

```json
{
  "message": "Record added successfully",
  "dataset": "employees",
  "recordId": 3
}
```

---

### 📊 Group Records

```http
GET /api/dataset/employees/query?groupBy=department
```

**Response**:

```json
{
  "groupedRecords": {
    "Engineering": [ {...}, {...} ],
    "HR": [ {...} ]
  }
}
```

---

### 🔽 Sort Records

```http
GET /api/dataset/employees/query?sortBy=age&order=desc
```

**Response**:

```json
{
  "sortedRecords": [
    { "id": 5, "name": "X", "age": 40,"department": "Engineering" },
    { "id": 3, "name": "Y", "age": 29 ,"department": "Engineering"}
  ]
}
```

---


## 🧪 Running Tests

```bash
mvn test
```

* Unit tests for service layer: `DatasetRecordServiceTest`
* (Optional) Add controller tests via `@WebMvcTest` for full coverage.

---

## 🐞 Error Handling

Handled via `@RestControllerAdvice`. Common responses:

| Error                            | HTTP Code                        |
| -------------------------------- | -------------------------------- |
| Missing `id` field               | 400                              |
| Invalid JSON structure           | 400                              |
| Group/Sort on non-existent field | 200 (with fallback or "UNKNOWN") |
| Internal exceptions              | 500                              |

---

## 📸 Output References

Screenshots of working Postman calls and table snapshots are saved in:

```
src/main/resources/static/
```

---

## 📌 Notes

* `id` is mandatory in all inserted JSONs.
* `datasetName` is dynamic and creates logical grouping.
* No frontend/UI, but fully testable via Postman or Swagger.

---


## 🙌 Author

Made with 💻 by Rajesh k

---
