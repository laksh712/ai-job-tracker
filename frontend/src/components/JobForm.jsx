import React, { useState } from 'react';

export default function JobForm({ onJobAdded, setLoading, setError }) {
  const [formData, setFormData] = useState({
    company: '',
    title: '',
    location: '',
    requiredSkills: '',
    requiredExperience: 0,
    status: 'OPEN'
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'requiredExperience' ? parseInt(value, 10) || 0 : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const res = await fetch('/api/jobs', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || `Failed to create job (${res.status})`);
      }

      // Reset form
      setFormData({
        company: '',
        title: '',
        location: '',
        requiredSkills: '',
        requiredExperience: 0,
        status: 'OPEN'
      });
      onJobAdded();
    } catch (err) {
      setError(err.message || 'Backend is unavailable');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>Add Job</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="form-group">
            <label>Company</label>
            <input
              type="text"
              name="company"
              required
              value={formData.company}
              onChange={handleChange}
              placeholder="e.g. Amazon"
            />
          </div>

          <div className="form-group">
            <label>Job Title</label>
            <input
              type="text"
              name="title"
              required
              value={formData.title}
              onChange={handleChange}
              placeholder="e.g. Java Backend Developer"
            />
          </div>

          <div className="form-group">
            <label>Location</label>
            <input
              type="text"
              name="location"
              required
              value={formData.location}
              onChange={handleChange}
              placeholder="e.g. Pune, India"
            />
          </div>

          <div className="form-group">
            <label>Required Skills (comma-separated)</label>
            <input
              type="text"
              name="requiredSkills"
              required
              value={formData.requiredSkills}
              onChange={handleChange}
              placeholder="e.g. Java, Spring Boot, MySQL"
            />
          </div>

          <div className="form-group">
            <label>Required Experience (Years)</label>
            <input
              type="number"
              name="requiredExperience"
              min="0"
              required
              value={formData.requiredExperience}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label>Status</label>
            <select name="status" value={formData.status} onChange={handleChange}>
              <option value="OPEN">OPEN</option>
              <option value="CLOSED">CLOSED</option>
            </select>
          </div>
        </div>

        <button type="submit" className="btn btn-primary">
          Add Job
        </button>
      </form>
    </div>
  );
}
