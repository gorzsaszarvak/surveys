package com.dynata.surveys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SurveyDto {
    private Long id;
    private String name;
    private Integer expectedCompletes;
    private Integer completionPoints;
    private Integer filteredPoints;


}
