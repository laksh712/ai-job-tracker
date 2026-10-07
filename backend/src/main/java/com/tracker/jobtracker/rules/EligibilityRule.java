package com.tracker.jobtracker.rules;

import com.tracker.jobtracker.entity.Job;

public interface EligibilityRule {
    boolean matches(Job job, int candidateExperience, String candidateSkills);
    String getFailureReason();
}
