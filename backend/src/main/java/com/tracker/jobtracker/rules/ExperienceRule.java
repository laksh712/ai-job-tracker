package com.tracker.jobtracker.rules;

import com.tracker.jobtracker.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class ExperienceRule implements EligibilityRule {

    private String failureReason;

    @Override
    public boolean matches(Job job, int candidateExperience, String candidateSkills) {
        int required = job.getRequiredExperience() != null ? job.getRequiredExperience() : 0;
        if (candidateExperience >= required) {
            failureReason = null;
            return true;
        }
        failureReason = String.format(
                "Candidate experience (%d years) does not meet required experience (%d years)",
                candidateExperience,
                required
        );
        return false;
    }

    @Override
    public String getFailureReason() {
        return failureReason;
    }
}
