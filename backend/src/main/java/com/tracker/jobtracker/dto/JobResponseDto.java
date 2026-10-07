package com.tracker.jobtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponseDto {
    private Long id;
    private String company;
    private String title;
    private String location;
    private String requiredSkills;
    private Integer requiredExperience;
    private String status;
    private LocalDateTime createdAt;
}
