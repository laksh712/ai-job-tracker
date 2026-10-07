# AI Job Tracker - Interview Demo Project

A production-grade, interview-friendly personal demonstration project combining a **Java Spring Boot backend**, **MySQL relational storage**, **clean layered architecture**, a **rules engine**, **custom middleware**, and a **Python FastAPI AI automation service**.

---

## 🏛️ System Architecture

```text
       HTTP Request
            │
            ▼
┌──────────────────────────┐
│  RequestLoggingFilter    │  (Execution time, HTTP method & URI logging)
└───────────┬──────────────┘
            │
            ▼
┌──────────────────────────┐
│      JobController       │  (REST API layer, HTTP status codes: 201, 200, 204, 404, 503)
└───────────┬──────────────┘
            │
            ▼
┌──────────────────────────┐      Calls HTTP       ┌──────────────────────────┐
│        JobService        │ ────────────────────► │   AiJobAnalyzerClient    │
└───────┬──────────┬───────┘   (Timeout: 3s connect │ (Spring RestClient)      │
        │          │            5s read)           └────────────┬─────────────┘
        │          │                                            │
        ▼          ▼                                            ▼
┌──────────────┐ ┌──────────────────────┐          ┌──────────────────────────┐
│  JobRepo     │ │ EligibilityRuleEngine│          │ Python FastAPI Service   │
│  (Data JPA)  │ │ (Experience & Skills)│          │ (POST /analyze-job)      │
└───────┬──────┘ └──────────────────────┘          │ Port: 8000               │
        │                                          └──────────────────────────┘
        ▼
┌──────────────────────────┐
│     MySQL Database       │ (ai_job_tracker @ 3306)
└──────────────────────────┘
```

---

## 🚀 Running the Services

### 1. Python AI Service (Port 8000)

```powershell
# In terminal 1:
cd "ai-service"

# Activate virtual environment
.\venv\Scripts\activate

# Run FastAPI with uvicorn
uvicorn app.main:app --reload --port 8000
```
- **Base URL:** `http://localhost:8000`
- **Swagger Documentation:** `http://localhost:8000/docs`
- **Health Check:** `http://localhost:8000/`

---

### 2. Java Spring Boot Backend (Port 8080)

```powershell
# In terminal 2:
cd "backend"

# Provide your local MySQL credentials
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "<your_mysql_password>"

# Run Spring Boot
mvn spring-boot:run
```
*(Or invoke via the full maven path if `mvn` is not on your PATH: `& "C:\Users\Admin\Downloads\apache-maven-3.9.12-bin\apache-maven-3.9.12\bin\mvn.cmd" spring-boot:run`)*

- **Base URL:** `http://localhost:8080`

---

### 3. React Frontend (Port 5173)

```powershell
# In terminal 3:
cd "frontend"

# Install dependencies (first time only)
npm install

# Start Vite dev server
npm run dev
```

- **Application URL:** `http://localhost:5173`


---

## 📡 API Endpoints

### 1. Core Job CRUD APIs
| Method | Path | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/jobs` | Create a job posting | `201 Created` |
| `GET` | `/api/jobs` | Retrieve all jobs | `200 OK` |
| `GET` | `/api/jobs/{id}` | Retrieve a job by ID | `200 OK` (or `404`) |
| `PUT` | `/api/jobs/{id}` | Update an existing job | `200 OK` (or `404`) |
| `DELETE` | `/api/jobs/{id}` | Delete a job | `204 No Content` |

### 2. Eligibility Rules Engine API
- **Endpoint:** `POST /api/jobs/{jobId}/eligibility`
- **Payload:**
```json
{
  "candidateExperience": 3,
  "candidateSkills": "Java, Spring Boot, MySQL"
}
```
- **Response:**
```json
{
  "jobId": 1,
  "status": "ELIGIBLE",
  "message": "Candidate meets the job requirements"
}
```

### 3. AI Job Analysis API (Spring Boot -> Python Integration)
- **Endpoint:** `POST /api/jobs/analyze`
- **Payload:**
```json
{
  "jobDescription": "We are looking for a Java developer with 2 years of experience in Spring Boot and MySQL."
}
```
- **Response:**
```json
{
  "skills": ["Java", "Spring Boot", "MySQL"],
  "experience": 2,
  "summary": "Java backend development role requiring 2+ years experience"
}
```

---

## 🛡️ Error Handling & Resiliency

- **Global Exception Handling:** Handled via `@RestControllerAdvice` in `GlobalExceptionHandler.java`.
- **AI Service Down / Unreachable:**
  - `AiJobAnalyzerClient` sets a connect timeout of 3s and read timeout of 5s.
  - If the Python service is offline or unreachable, an `AiServiceException` is thrown.
  - The server logs the technical error internally and returns a clean `503 Service Unavailable` with a structured `ErrorResponseDto` (no internal stack traces exposed to client).
