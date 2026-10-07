package com.tracker.jobtracker.rules;

import com.tracker.jobtracker.entity.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EligibilityRuleEngineTest {

    private EligibilityRuleEngine ruleEngine;
    private Job targetJob;

    @BeforeEach
    void setUp() {
        ExperienceRule experienceRule = new ExperienceRule();
        SkillsRule skillsRule = new SkillsRule();
        ruleEngine = new EligibilityRuleEngine(List.of(experienceRule, skillsRule));

        targetJob = Job.builder()
                .id(1L)
                .company("Amazon")
                .title("Java Developer")
                .location("Pune")
                .requiredExperience(2)
                .requiredSkills("Java, Spring Boot, MySQL")
                .build();
    }

    @Test
    void testEligibleCandidate() {
        // Candidate has 3 years of experience and all required skills (different casing/spacing)
        EligibilityRuleEngine.EligibilityResult result = ruleEngine.evaluate(
                targetJob,
                3,
                "java, spring boot, mysql, docker"
        );

        assertThat(result.status()).isEqualTo(EligibilityStatus.ELIGIBLE);
        assertThat(result.message()).isEqualTo("Candidate meets the job requirements");
    }

    @Test
    void testInsufficientExperience() {
        // Candidate has 1 year experience but required is 2
        EligibilityRuleEngine.EligibilityResult result = ruleEngine.evaluate(
                targetJob,
                1,
                "Java, Spring Boot, MySQL"
        );

        assertThat(result.status()).isEqualTo(EligibilityStatus.NOT_ELIGIBLE);
        assertThat(result.message()).contains("Candidate experience (1 years) does not meet required experience (2 years)");
    }

    @Test
    void testMissingRequiredSkill() {
        // Candidate has enough experience (4 years) but misses 'MySQL'
        EligibilityRuleEngine.EligibilityResult result = ruleEngine.evaluate(
                targetJob,
                4,
                "Java, Spring Boot"
        );

        assertThat(result.status()).isEqualTo(EligibilityStatus.NOT_ELIGIBLE);
        assertThat(result.message()).contains("Candidate is missing required skills: MySQL");
    }
}
