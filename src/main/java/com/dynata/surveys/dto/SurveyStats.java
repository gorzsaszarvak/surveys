package com.dynata.surveys.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyStats {
    private Long surveyId;
    private String surveyName;
    private int completes;
    private int filteredParticipants;
    private int rejectedParticipants;
    private double averageLength;
}
