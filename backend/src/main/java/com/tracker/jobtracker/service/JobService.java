package com.tracker.jobtracker.service;

import com.tracker.jobtracker.dto.JobRequestDto;
import com.tracker.jobtracker.dto.JobResponseDto;
import com.tracker.jobtracker.entity.Job;
import com.tracker.jobtracker.exception.ResourceNotFoundException;
import com.tracker.jobtracker.repository.JobRepository;
import com.tracker.jobtracker.dto.AiJobAnalyzeRequest;
import com.tracker.jobtracker.dto.AiJobAnalyzeResponse;
import com.tracker.jobtracker.dto.EligibilityRequestDto;
import com.tracker.jobtracker.dto.EligibilityResponseDto;
import com.tracker.jobtracker.rules.EligibilityRuleEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final EligibilityRuleEngine eligibilityRuleEngine;
    private final AiJobAnalyzerClient aiJobAnalyzerClient;

    @Transactional
    public JobResponseDto createJob(JobRequestDto requestDto) {
        Job job = Job.builder()
                .company(requestDto.getCompany())
                .title(requestDto.getTitle())
                .location(requestDto.getLocation())
                .requiredSkills(requestDto.getRequiredSkills())
                .requiredExperience(requestDto.getRequiredExperience() != null ? requestDto.getRequiredExperience() : 0)
                .status(requestDto.getStatus() != null && !requestDto.getStatus().isBlank() ? requestDto.getStatus() : "OPEN")
                .build();

        Job savedJob = jobRepository.save(job);
        return mapToResponseDto(savedJob);
    }

    @Transactional(readOnly = true)
    public List<JobResponseDto> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JobResponseDto getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        return mapToResponseDto(job);
    }

    @Transactional
    public JobResponseDto updateJob(Long id, JobRequestDto requestDto) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        existingJob.setCompany(requestDto.getCompany());
        existingJob.setTitle(requestDto.getTitle());
        existingJob.setLocation(requestDto.getLocation());
        existingJob.setRequiredSkills(requestDto.getRequiredSkills());
        if (requestDto.getRequiredExperience() != null) {
            existingJob.setRequiredExperience(requestDto.getRequiredExperience());
        }
        if (requestDto.getStatus() != null && !requestDto.getStatus().isBlank()) {
            existingJob.setStatus(requestDto.getStatus());
        }

        Job updatedJob = jobRepository.save(existingJob);
        return mapToResponseDto(updatedJob);
    }

    @Transactional
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        jobRepository.delete(job);
    }

    @Transactional(readOnly = true)
    public EligibilityResponseDto evaluateEligibility(Long jobId, EligibilityRequestDto requestDto) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        int experience = requestDto.getCandidateExperience() != null ? requestDto.getCandidateExperience() : 0;
        EligibilityRuleEngine.EligibilityResult result = eligibilityRuleEngine.evaluate(
                job,
                experience,
                requestDto.getCandidateSkills()
        );

        return EligibilityResponseDto.builder()
                .jobId(job.getId())
                .status(result.status())
                .message(result.message())
                .build();
    }

    public AiJobAnalyzeResponse analyzeJobDescription(AiJobAnalyzeRequest request) {
        return aiJobAnalyzerClient.analyzeJob(request);
    }

    private JobResponseDto mapToResponseDto(Job job) {
        return JobResponseDto.builder()
                .id(job.getId())
                .company(job.getCompany())
                .title(job.getTitle())
                .location(job.getLocation())
                .requiredSkills(job.getRequiredSkills())
                .requiredExperience(job.getRequiredExperience())
                .status(job.getStatus())
                .createdAt(job.getCreatedAt())
                .build();
    }
}
