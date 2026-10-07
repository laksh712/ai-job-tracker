import React from 'react';

export default function JobList({ jobs, onRefresh, onDelete, loading }) {
  return (
    <div className="card">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <h2>Job Listings</h2>
        <button className="btn btn-secondary" onClick={onRefresh} disabled={loading}>
          Refresh
        </button>
      </div>

      {jobs.length === 0 ? (
        <p style={{ color: '#718096' }}>No jobs found. Add a job above.</p>
      ) : (
        <div className="table-container">
          <table className="job-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Company</th>
                <th>Title</th>
                <th>Location</th>
                <th>Skills</th>
                <th>Exp (Yrs)</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {jobs.map((job) => (
                <tr key={job.id}>
                  <td>{job.id}</td>
                  <td><strong>{job.company}</strong></td>
                  <td>{job.title}</td>
                  <td>{job.location}</td>
                  <td>{job.requiredSkills}</td>
                  <td>{job.requiredExperience}</td>
                  <td>
                    <span className={`badge ${job.status === 'OPEN' ? 'badge-open' : 'badge-closed'}`}>
                      {job.status}
                    </span>
                  </td>
                  <td>
                    <button
                      className="btn btn-danger"
                      onClick={() => onDelete(job.id)}
                      disabled={loading}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
