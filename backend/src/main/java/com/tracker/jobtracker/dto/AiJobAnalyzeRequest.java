package com.tracker.jobtracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiJobAnalyzeRequest {

    @NotBlank(message = "Job description cannot be blank")
    private String jobDescription;
}
