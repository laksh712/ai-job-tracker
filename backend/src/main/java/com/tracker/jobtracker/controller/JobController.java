package com.tracker.jobtracker.controller;

import com.tracker.jobtracker.dto.AiJobAnalyzeRequest;
import com.tracker.jobtracker.dto.AiJobAnalyzeResponse;
import com.tracker.jobtracker.dto.EligibilityRequestDto;
import com.tracker.jobtracker.dto.EligibilityResponseDto;
import com.tracker.jobtracker.dto.JobRequestDto;
import com.tracker.jobtracker.dto.JobResponseDto;
import com.tracker.jobtracker.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponseDto> createJob(@Valid @RequestBody JobRequestDto requestDto) {
        JobResponseDto createdJob = jobService.createJob(requestDto);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobResponseDto>> getAllJobs() {
        List<JobResponseDto> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDto> getJobById(@PathVariable Long id) {
        JobResponseDto job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDto> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequestDto requestDto
    ) {
        JobResponseDto updatedJob = jobService.updateJob(id, requestDto);
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{jobId}/eligibility")
    public ResponseEntity<EligibilityResponseDto> evaluateEligibility(
            @PathVariable Long jobId,
            @Valid @RequestBody EligibilityRequestDto requestDto
    ) {
        EligibilityResponseDto response = jobService.evaluateEligibility(jobId, requestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/analyze")
    public ResponseEntity<AiJobAnalyzeResponse> analyzeJobDescription(
            @Valid @RequestBody AiJobAnalyzeRequest request
    ) {
        AiJobAnalyzeResponse response = jobService.analyzeJobDescription(request);
        return ResponseEntity.ok(response);
    }
}
