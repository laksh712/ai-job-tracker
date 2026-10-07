package com.tracker.jobtracker.rules;

import com.tracker.jobtracker.entity.Job;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SkillsRule implements EligibilityRule {

    private String failureReason;

    @Override
    public boolean matches(Job job, int candidateExperience, String candidateSkills) {
        if (job.getRequiredSkills() == null || job.getRequiredSkills().isBlank()) {
            failureReason = null;
            return true;
        }

        Set<String> candidateSkillSet = normalizeSkills(candidateSkills);
        List<String> requiredSkillList = Arrays.stream(job.getRequiredSkills().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        List<String> missingSkills = requiredSkillList.stream()
                .filter(skill -> !candidateSkillSet.contains(skill.toLowerCase()))
                .collect(Collectors.toList());

        if (missingSkills.isEmpty()) {
            failureReason = null;
            return true;
        }

        failureReason = "Candidate is missing required skills: " + String.join(", ", missingSkills);
        return false;
    }

    @Override
    public String getFailureReason() {
        return failureReason;
    }

    private Set<String> normalizeSkills(String skills) {
        if (skills == null || skills.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(skills.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
