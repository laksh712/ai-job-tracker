# AI Job Tracker - Python AI Service

A lightweight FastAPI service for analyzing job descriptions, extracting required skills, estimating experience requirements, and producing structured summaries.

## Folder Structure

```text
ai-service/
├── app/
│   ├── __init__.py
│   ├── main.py              # FastAPI application instance & routing
│   ├── schemas.py           # Pydantic request/response validation
│   └── services/
│       ├── __init__.py
│       └── analyzer_service.py # Core extraction logic (rule-based / LLM ready)
├── requirements.txt         # Minimal production dependencies
├── .env.example             # Configuration template
└── README.md
```

## Setup & Running

### 1. Create a Virtual Environment
```bash
python -m venv venv
```

### 2. Activate on Windows
```powershell
venv\Scripts\activate
```

### 3. Install Dependencies
```bash
pip install -r requirements.txt
```

### 4. Run the Service
```bash
uvicorn app.main:app --reload --port 8000
```

### 5. Interactive API Documentation (Swagger)
Open your browser to:
- Swagger UI: [http://localhost:8000/docs](http://localhost:8000/docs)
- ReDoc: [http://localhost:8000/redoc](http://localhost:8000/redoc)

## Endpoint: Analyze Job

- **Route:** `POST /analyze-job`
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
