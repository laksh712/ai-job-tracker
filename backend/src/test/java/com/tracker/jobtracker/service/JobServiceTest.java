package com.tracker.jobtracker.service;

import com.tracker.jobtracker.dto.JobRequestDto;
import com.tracker.jobtracker.dto.JobResponseDto;
import com.tracker.jobtracker.entity.Job;
import com.tracker.jobtracker.exception.ResourceNotFoundException;
import com.tracker.jobtracker.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private com.tracker.jobtracker.rules.EligibilityRuleEngine eligibilityRuleEngine;

    @Mock
    private AiJobAnalyzerClient aiJobAnalyzerClient;

    @InjectMocks
    private JobService jobService;

    private Job sampleJob;
    private JobRequestDto sampleRequest;

    @BeforeEach
    void setUp() {
        sampleJob = Job.builder()
                .id(1L)
                .company("Google")
                .title("Software Engineer")
                .location("Bangalore")
                .requiredSkills("Java, Spring Boot")
                .requiredExperience(3)
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .build();

        sampleRequest = JobRequestDto.builder()
                .company("Google")
                .title("Software Engineer")
                .location("Bangalore")
                .requiredSkills("Java, Spring Boot")
                .requiredExperience(3)
                .status("OPEN")
                .build();
    }

    @Test
    void testCreateJob_Success() {
        when(jobRepository.save(any(Job.class))).thenReturn(sampleJob);

        JobResponseDto response = jobService.createJob(sampleRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCompany()).isEqualTo("Google");
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testGetAllJobs_Success() {
        when(jobRepository.findAll()).thenReturn(List.of(sampleJob));

        List<JobResponseDto> result = jobService.getAllJobs();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Software Engineer");
    }

    @Test
    void testGetJobById_Found() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(sampleJob));

        JobResponseDto response = jobService.getJobById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void testGetJobById_NotFound_ThrowsException() {
        when(jobRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobService.getJobById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Job not found with id: 99");
    }

    @Test
    void testDeleteJob_Success() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(sampleJob));
        doNothing().when(jobRepository).delete(sampleJob);

        jobService.deleteJob(1L);

        verify(jobRepository, times(1)).delete(sampleJob);
    }

    @Test
    void testEvaluateEligibility_Success() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(sampleJob));
        when(eligibilityRuleEngine.evaluate(sampleJob, 3, "Java, Spring Boot"))
                .thenReturn(new com.tracker.jobtracker.rules.EligibilityRuleEngine.EligibilityResult(
                        com.tracker.jobtracker.rules.EligibilityStatus.ELIGIBLE,
                        "Candidate meets the job requirements"
                ));

        var request = new com.tracker.jobtracker.dto.EligibilityRequestDto(3, "Java, Spring Boot");
        var response = jobService.evaluateEligibility(1L, request);

        assertThat(response).isNotNull();
        assertThat(response.getJobId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(com.tracker.jobtracker.rules.EligibilityStatus.ELIGIBLE);
    }

    @Test
    void testAnalyzeJobDescription_Success() {
        var aiRequest = new com.tracker.jobtracker.dto.AiJobAnalyzeRequest("Need Java developer");
        var aiResponse = com.tracker.jobtracker.dto.AiJobAnalyzeResponse.builder()
                .skills(List.of("Java"))
                .experience(2)
                .summary("Java role")
                .build();

        when(aiJobAnalyzerClient.analyzeJob(aiRequest)).thenReturn(aiResponse);

        var result = jobService.analyzeJobDescription(aiRequest);

        assertThat(result).isNotNull();
        assertThat(result.getSkills()).contains("Java");
        assertThat(result.getExperience()).isEqualTo(2);
        assertThat(result.getSummary()).isEqualTo("Java role");
    }
}
