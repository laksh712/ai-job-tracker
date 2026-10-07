package com.tracker.jobtracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityRequestDto {

    @Min(value = 0, message = "Candidate experience must be 0 or greater")
    private Integer candidateExperience;

    @NotBlank(message = "Candidate skills must be provided")
    private String candidateSkills;
}
