package com.tracker.jobtracker.rules;

import com.tracker.jobtracker.entity.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EligibilityRuleEngine {

    private final List<EligibilityRule> rules;

    public EligibilityResult evaluate(Job job, int candidateExperience, String candidateSkills) {
        List<String> failureReasons = new ArrayList<>();

        for (EligibilityRule rule : rules) {
            boolean passed = rule.matches(job, candidateExperience, candidateSkills);
            if (!passed && rule.getFailureReason() != null) {
                failureReasons.add(rule.getFailureReason());
            }
        }

        if (failureReasons.isEmpty()) {
            return new EligibilityResult(
                    EligibilityStatus.ELIGIBLE,
                    "Candidate meets the job requirements"
            );
        } else {
            return new EligibilityResult(
                    EligibilityStatus.NOT_ELIGIBLE,
                    String.join("; ", failureReasons)
            );
        }
    }

    public record EligibilityResult(EligibilityStatus status, String message) {}
}
