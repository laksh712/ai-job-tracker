import os
from dotenv import load_dotenv
from fastapi import FastAPI, status
from app.schemas import JobAnalyzeRequest, JobAnalyzeResponse
from app.services.analyzer_service import JobAnalyzerService

load_dotenv()

app = FastAPI(
    title="AI Job Tracker - AI Automation Service",
    description="Microservice for extracting skills, experience, and summaries from job postings.",
    version="1.0.0"
)

analyzer_service = JobAnalyzerService()

@app.get("/", status_code=status.HTTP_200_OK)
def health_check():
    return {"status": "ok", "service": "ai-service"}

@app.post("/analyze-job", response_model=JobAnalyzeResponse, status_code=status.HTTP_200_OK)
def analyze_job(request: JobAnalyzeRequest):
    return analyzer_service.analyze(request.jobDescription)
