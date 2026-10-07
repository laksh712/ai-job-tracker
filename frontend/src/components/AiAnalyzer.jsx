import React, { useState } from 'react';

export default function AiAnalyzer({ setLoading, setError }) {
  const [description, setDescription] = useState('');
  const [result, setResult] = useState(null);

  const handleAnalyze = async () => {
    if (!description.trim()) {
      setError('Please enter a job description to analyze.');
      return;
    }

    setError(null);
    setLoading(true);
    setResult(null);

    try {
      const res = await fetch('/api/jobs/analyze', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ jobDescription: description })
      });

      if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        if (res.status === 503) {
          throw new Error('AI analysis service is currently unavailable. Please make sure the Python service is running.');
        }
        throw new Error(errorData.message || `AI analysis failed (${res.status})`);
      }

      const data = await res.json();
      setResult(data);
    } catch (err) {
      setError(err.message || 'Failed to connect to backend/AI service');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>AI Job Analyzer</h2>
      <p style={{ fontSize: '13px', color: '#718096', marginBottom: '10px' }}>
        Paste a job description below to extract key skills, experience level, and generate a role summary.
      </p>

      <textarea
        className="textarea-input"
        placeholder="e.g. We are looking for a Java developer with 2 years of experience in Spring Boot and MySQL."
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />

      <button className="btn btn-primary" onClick={handleAnalyze}>
        Analyze with AI
      </button>

      {result && (
        <div className="analysis-box">
          <h3>Analysis Result</h3>
          <div className="analysis-item">
            <strong>Summary: </strong>
            <span>{result.summary}</span>
          </div>
          <div className="analysis-item">
            <strong>Experience: </strong>
            <span>{result.experience} year(s)</span>
          </div>
          <div className="analysis-item">
            <strong>Detected Skills: </strong>
            <div>
              {result.skills && result.skills.length > 0 ? (
                result.skills.map((skill, idx) => (
                  <span key={idx} className="skill-tag">
                    {skill}
                  </span>
                ))
              ) : (
                <span style={{ color: '#a0aec0' }}>None detected</span>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
