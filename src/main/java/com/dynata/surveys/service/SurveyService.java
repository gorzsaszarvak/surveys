package com.dynata.surveys.service;

import com.dynata.surveys.dto.MemberDto;
import com.dynata.surveys.dto.SurveyDto;
import com.dynata.surveys.dto.SurveyStats;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SurveyService {
    List<MemberDto> getRespondentsForSurvey(Long surveyId);

    List<SurveyDto> getCompletedSurveysForMember(Long memberId);

    Map<Long, Integer> getPointsForMember(Long memberId);

    List<MemberDto> getEligibleMembersForSurvey(Long surveyId);

    List<SurveyStats> getSurveysWithStats();


}
