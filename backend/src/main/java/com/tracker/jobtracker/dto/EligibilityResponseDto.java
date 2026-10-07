package com.tracker.jobtracker.dto;

import com.tracker.jobtracker.rules.EligibilityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityResponseDto {
    private Long jobId;
    private EligibilityStatus status;
    private String message;
}
