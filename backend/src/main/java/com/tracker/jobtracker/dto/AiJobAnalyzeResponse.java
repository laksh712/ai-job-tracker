package com.tracker.jobtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiJobAnalyzeResponse {
    private List<String> skills;
    private Integer experience;
    private String summary;
}
