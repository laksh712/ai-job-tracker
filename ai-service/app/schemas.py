from typing import List
from pydantic import BaseModel, Field

class JobAnalyzeRequest(BaseModel):
    jobDescription: str = Field(
        ...,
        min_length=5,
        description="Raw text of the job description to analyze"
    )

    model_config = {
        "json_schema_extra": {
            "example": {
                "jobDescription": "We are looking for a Java developer with 2 years of experience in Spring Boot and MySQL."
            }
        }
    }


class JobAnalyzeResponse(BaseModel):
    skills: List[str] = Field(
        default_factory=list,
        description="List of detected skills required for the job"
    )
    experience: int = Field(
        ...,
        ge=0,
        description="Minimum years of experience required"
    )
    summary: str = Field(
        ...,
        description="Generated summary of the role"
    )

    model_config = {
        "json_schema_extra": {
            "example": {
                "skills": ["Java", "Spring Boot", "MySQL"],
                "experience": 2,
                "summary": "Java backend development role"
            }
        }
    }
