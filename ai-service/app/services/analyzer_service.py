import re
from typing import List, Tuple
from app.schemas import JobAnalyzeResponse

class JobAnalyzerService:
    """
    Extracts skills, experience, and role summary from job descriptions.
    Currently implements a clean rule-based heuristic that can be replaced or
    augmented by an LLM client without altering the API contract.
    """

    KNOWN_SKILLS = [
        "Java",
        "Spring Boot",
        "Python",
        "React",
        "MySQL",
        "PostgreSQL",
        "AWS",
        "Docker",
        "Kubernetes",
        "Git",
        "REST API"
    ]

    def analyze(self, job_description: str) -> JobAnalyzeResponse:
        skills = self._extract_skills(job_description)
        experience = self._extract_experience(job_description)
        summary = self._generate_summary(skills, experience)

        return JobAnalyzeResponse(
            skills=skills,
            experience=experience,
            summary=summary
        )

    def _extract_skills(self, text: str) -> List[str]:
        matched_skills = []
        for skill in self.KNOWN_SKILLS:
            # Word boundary regex with case-insensitive matching
            pattern = r"(?<!\w)" + re.escape(skill) + r"(?!\w)"
            if re.search(pattern, text, re.IGNORECASE):
                matched_skills.append(skill)
        return matched_skills

    def _extract_experience(self, text: str) -> int:
        """
        Detects experience mentions like:
        '2 years', '2+ years', '3-5 years', 'at least 4 years of experience'
        """
        patterns = [
            r"(\d+)\s*\+?\s*(?:to|-)\s*\d+\s*years?",  # e.g. 3-5 years -> 3
            r"(\d+)\s*\+?\s*years?\s*(?:of)?\s*experience", # e.g. 2 years of experience
            r"(\d+)\s*\+?\s*years?", # e.g. 2 years, 2+ years
            r"experience\s*(?:of)?\s*(\d+)\s*\+?\s*years?"
        ]

        for pat in patterns:
            match = re.search(pat, text, re.IGNORECASE)
            if match:
                try:
                    return int(match.group(1))
                except (ValueError, IndexError):
                    pass

        return 0

    def _generate_summary(self, skills: List[str], experience: int) -> str:
        # Determine dominant tech domain
        skills_lower = [s.lower() for s in skills]
        if "java" in skills_lower or "spring boot" in skills_lower:
            role_type = "Java backend development role"
        elif "python" in skills_lower:
            role_type = "Python software engineering role"
        elif "react" in skills_lower:
            role_type = "Frontend development role"
        else:
            role_type = "Software engineering role"

        if experience > 0:
            return f"{role_type} requiring {experience}+ years experience"
        return role_type
