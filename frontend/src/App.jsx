import React, { useState, useEffect } from 'react';
import JobForm from './components/JobForm';
import JobList from './components/JobList';
import AiAnalyzer from './components/AiAnalyzer';

export default function App() {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchJobs = async () => {
    setError(null);
    setLoading(true);
    try {
      const res = await fetch('/api/jobs');
      if (!res.ok) {
        throw new Error(`Failed to load jobs from backend (Status: ${res.status})`);
      }
      const data = await res.json();
      setJobs(data);
    } catch (err) {
      setError(err.message || 'Backend is currently unreachable');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteJob = async (id) => {
    if (!window.confirm('Are you sure you want to delete this job?')) return;
    setError(null);
    setLoading(true);
    try {
      const res = await fetch(`/api/jobs/${id}`, {
        method: 'DELETE'
      });
      if (!res.ok && res.status !== 204) {
        throw new Error(`Failed to delete job (Status: ${res.status})`);
      }
      // Refresh list
      fetchJobs();
    } catch (err) {
      setError(err.message || 'Error deleting job');
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchJobs();
  }, []);

  return (
    <div className="container">
      <header className="header">
        <h1>AI Job Tracker</h1>
        <p>Interview Demo: Spring Boot + MySQL + Rules Engine + Python AI Service</p>
      </header>

      {error && <div className="alert-error">{error}</div>}
      {loading && <div className="loading-text">Loading...</div>}

      {/* 1. Add Job Form */}
      <JobForm onJobAdded={fetchJobs} setLoading={setLoading} setError={setError} />

      {/* 2. Job Listings */}
      <JobList
        jobs={jobs}
        onRefresh={fetchJobs}
        onDelete={handleDeleteJob}
        loading={loading}
      />

      {/* 3. AI Job Analyzer */}
      <AiAnalyzer setLoading={setLoading} setError={setError} />
    </div>
  );
}
