from app.services.analyzer_service import JobAnalyzerService

def test_job_analyzer_java_role():
    service = JobAnalyzerService()
    text = "We are looking for a Java developer with 2 years of experience in Spring Boot and MySQL."
    result = service.analyze(text)

    assert "Java" in result.skills
    assert "Spring Boot" in result.skills
    assert "MySQL" in result.skills
    assert result.experience == 2
    assert "Java backend development role" in result.summary
    print("Test 1 Passed: Java role analyzed correctly!")

def test_job_analyzer_no_experience_specified():
    service = JobAnalyzerService()
    text = "Looking for a React developer with knowledge of Docker and Git."
    result = service.analyze(text)

    assert "React" in result.skills
    assert "Docker" in result.skills
    assert result.experience == 0
    assert "Frontend development role" in result.summary
    print("Test 2 Passed: React role analyzed correctly!")

if __name__ == "__main__":
    test_job_analyzer_java_role()
    test_job_analyzer_no_experience_specified()
    print("All service tests passed!")
