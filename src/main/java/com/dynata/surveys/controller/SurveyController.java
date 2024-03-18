package com.dynata.surveys.controller;

import com.dynata.surveys.dto.MemberDto;
import com.dynata.surveys.dto.SurveyDto;
import com.dynata.surveys.dto.SurveyStats;
import com.dynata.surveys.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @GetMapping("surveys/respondents/{surveyId}")
    public List<MemberDto> getRespondentsForSurvey(@PathVariable Long surveyId) {
        return surveyService.getRespondentsForSurvey(surveyId);
    }

    @GetMapping("members/completed/{memberId}")
    public List<SurveyDto> getCompletedSurveysForMember(@PathVariable Long memberId) {
        return surveyService.getCompletedSurveysForMember(memberId);
    }

    @GetMapping("members/points/{memberId}")
    public Map<Long, Integer> getPointsForMember(@PathVariable Long memberId) {
        return surveyService.getPointsForMember(memberId);
    }

    @GetMapping("surveys/eligibleMembers/{surveyId}")
    public List<MemberDto> getEligibleMembersForSurvey(@PathVariable Long surveyId) {
        return surveyService.getEligibleMembersForSurvey(surveyId);
    }

    @GetMapping("surveys/stats")
    public List<SurveyStats> getSurveysWithStats() {
        return surveyService.getSurveysWithStats();
    }
}