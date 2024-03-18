package com.dynata.surveys.service;

import com.dynata.surveys.domain.Member;
import com.dynata.surveys.domain.Participation;
import com.dynata.surveys.domain.Status;
import com.dynata.surveys.domain.Survey;
import com.dynata.surveys.dto.MemberDto;
import com.dynata.surveys.dto.SurveyDto;
import com.dynata.surveys.dto.SurveyStats;
import com.dynata.surveys.repository.MemberRepository;
import com.dynata.surveys.repository.ParticipationRepository;
import com.dynata.surveys.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SurveyServiceImplTest {

    @InjectMocks
    private SurveyServiceImpl serviceUnderTest;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private SurveyRepository surveyRepository;
    @Mock
    private ParticipationRepository participationRepository;


    @Test
    void testGetRespondentsForSurveyReturnsCompletedMembers() {
        //Given
        Long surveyId = 1L;
        Long memberId = 1L;
        Member member = mock(Member.class);
        Participation participation = mock(Participation.class);

        when(participationRepository.findAllBySurvey_Id(surveyId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.COMPLETED);
        when(participation.getMember()).thenReturn(member);

        when(member.getId()).thenReturn(memberId);

        //When
        List<MemberDto> respondents = serviceUnderTest.getRespondentsForSurvey(surveyId);

        //Then
        assertNotNull(respondents);
        assertEquals(1, respondents.size());
        assertEquals(memberId, respondents.get(0).getId());
    }

    @Test
    void testGetRespondentsForSurveyExcludesFilteredMembers() {
        //Given
        Long surveyId = 1L;
        Participation participation = mock(Participation.class);

        when(participationRepository.findAllBySurvey_Id(surveyId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.FILTERED);

        //When
        List<MemberDto> respondents = serviceUnderTest.getRespondentsForSurvey(surveyId);

        //Then
        assertNotNull(respondents);
        assertTrue(respondents.isEmpty());


    }
    @Test
    void testGetRespondentsForSurveyExcludesRejectedMembers() {
        //Given
        Long surveyId = 1L;
        Participation participation = mock(Participation.class);

        when(participationRepository.findAllBySurvey_Id(surveyId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.REJECTED);

        //When
        List<MemberDto> respondents = serviceUnderTest.getRespondentsForSurvey(surveyId);

        //Then
        assertNotNull(respondents);
        assertTrue(respondents.isEmpty());


    }

    @Test
    void testGetCompletedSurveysForMemberReturnsCompletedSurveys() {
        //Given
        Long memberId = 1L;
        Long surveyId = 1L;
        Participation participation = mock(Participation.class);
        Survey survey = mock(Survey.class);

        when(participationRepository.findAllByMember_Id(memberId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.COMPLETED);
        when(participation.getSurvey()).thenReturn(survey);

        when(survey.getId()).thenReturn(surveyId);

        //When
        List<SurveyDto> completedSurveys = serviceUnderTest.getCompletedSurveysForMember(memberId);

        //Then
        assertNotNull(completedSurveys);
        assertEquals(1, completedSurveys.size());
        assertEquals(surveyId, completedSurveys.get(0).getId());
    }

    @Test
    void testGetCompletedSurveysForMemberExcludesFilteredSurveys() {
        //Given
        Long memberId = 1L;
        Participation participation = mock(Participation.class);

        when(participationRepository.findAllByMember_Id(memberId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.FILTERED);

        //When
        List<SurveyDto> completedSurveys = serviceUnderTest.getCompletedSurveysForMember(memberId);

        //Then
        assertNotNull(completedSurveys);
        assertTrue(completedSurveys.isEmpty());
    }

    @Test
    void testGetCompletedSurveysForMemberExcludesRejectedSurveys() {
        //Given
        Long memberId = 1L;
        Participation participation = mock(Participation.class);

        when(participationRepository.findAllByMember_Id(memberId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.REJECTED);

        //When
        List<SurveyDto> completedSurveys = serviceUnderTest.getCompletedSurveysForMember(memberId);

        //Then
        assertNotNull(completedSurveys);
        assertTrue(completedSurveys.isEmpty());
    }

    @Test
    void testGetPointsForMemberReturnsCompletedSurveyPoints() {
        //When
        Long memberId = 1L;
        Participation participation = mock(Participation.class);
        Survey survey = mock(Survey.class);
        Long surveyId = 1L;
        Integer surveyPoints = 10;


        when(participationRepository.findAllByMember_Id(memberId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.COMPLETED);
        when(participation.getSurvey()).thenReturn(survey);
        when(survey.getCompletionPoints()).thenReturn(surveyPoints);
        when(survey.getId()).thenReturn(surveyId);

        //When
        Map<Long, Integer> pointsBySurvey = serviceUnderTest.getPointsForMember(memberId);

        //Then
        assertNotNull(pointsBySurvey);
        assertEquals(1, pointsBySurvey.size());
        assertTrue(pointsBySurvey.containsKey(surveyId));
        assertTrue(pointsBySurvey.containsValue(surveyPoints));
    }

    @Test
    void testGetPointsForMemberReturnsFilteredSurveyPoints() {
        //When
        Long memberId = 1L;
        Participation participation = mock(Participation.class);
        Survey survey = mock(Survey.class);
        Long surveyId = 1L;
        Integer surveyPoints = 10;


        when(participationRepository.findAllByMember_Id(memberId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.FILTERED);
        when(participation.getSurvey()).thenReturn(survey);
        when(survey.getFilteredPoints()).thenReturn(surveyPoints);
        when(survey.getId()).thenReturn(surveyId);

        //When
        Map<Long, Integer> pointsBySurvey = serviceUnderTest.getPointsForMember(memberId);

        //Then
        assertNotNull(pointsBySurvey);
        assertEquals(1, pointsBySurvey.size());
        assertTrue(pointsBySurvey.containsKey(surveyId));
        assertTrue(pointsBySurvey.containsValue(surveyPoints));
    }

    @Test
    void testGetPointsForMemberExcludesRejectedSurvey() {
        //Given
        Long memberId = 1L;
        Participation participation = mock(Participation.class);

        when(participationRepository.findAllByMember_Id(memberId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.REJECTED);

        //When
        Map<Long, Integer> pointsBySurvey = serviceUnderTest.getPointsForMember(memberId);

        //Then
        assertNotNull(pointsBySurvey);
        assertTrue(pointsBySurvey.isEmpty());
    }

    @Test
    void testGetEligibleMembersForSurveyReturnAvailableNotParticipatedMember() {
        //Given
        Long surveyId = 1L;
        Long memberId = 1L;
        Member member = mock(Member.class);

        when(memberRepository.findAll()).thenReturn(List.of(member));

        when(member.isActive()).thenReturn(true);
        when(member.getId()).thenReturn(memberId);

        when(participationRepository.findAllBySurvey_Id(surveyId)).thenReturn(List.of());

        //When
        List<MemberDto> eligibleMembers = serviceUnderTest.getEligibleMembersForSurvey(surveyId);

        //Then
        assertNotNull(eligibleMembers);
        assertEquals(1, eligibleMembers.size());
        assertEquals(memberId, eligibleMembers.get(0).getId());
    }
    @Test
    void testGetEligibleMembersForSurveyExcludesInactiveMember() {
        //Given
        Long surveyId = 1L;
        Member member = mock(Member.class);

        when(memberRepository.findAll()).thenReturn(List.of(member));

        when(member.isActive()).thenReturn(false);

        //When
        List<MemberDto> eligibleMembers = serviceUnderTest.getEligibleMembersForSurvey(surveyId);

        //Then
        assertNotNull(eligibleMembers);
        assertTrue(eligibleMembers.isEmpty());
    }
    @Test
    void testGetEligibleMembersForSurveyExcludesAlreadyParticipatedMember() {
        //Given
        Long surveyId = 1L;
        Long memberId = 1L;
        Member member = mock(Member.class);
        Participation participation = mock(Participation.class);

        when(memberRepository.findAll()).thenReturn(List.of(member));

        when(member.isActive()).thenReturn(true);
        when(member.getId()).thenReturn(memberId);

        when(participationRepository.findAllBySurvey_Id(surveyId)).thenReturn(List.of(participation));

        when(participation.getMember()).thenReturn(member);

        //When
        List<MemberDto> eligibleMembers = serviceUnderTest.getEligibleMembersForSurvey(surveyId);

        //Then
        assertNotNull(eligibleMembers);
        assertTrue(eligibleMembers.isEmpty());
    }

    @Test
    void testGetSurveysWithStatsReturnsCorrectStatsForCompletedParticipants() {
        //Given
        Long surveyId = 1L;
        Survey survey = mock(Survey.class);
        String surveyName = "survey";
        Participation participation = mock(Participation.class);
        Integer length = 10;

        when(surveyRepository.findAll()).thenReturn(List.of(survey));

        when(survey.getId()).thenReturn(surveyId);
        when(survey.getName()).thenReturn(surveyName);

        when(participationRepository.findAllBySurvey_Id(surveyId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.COMPLETED);
        when(participation.getLength()).thenReturn(Optional.of(length));

        //When
        List<SurveyStats> surveyStatsList = serviceUnderTest.getSurveysWithStats();

        //Then
        assertNotNull(surveyStatsList);
        assertEquals(1, surveyStatsList.size());
        assertEquals(surveyId, surveyStatsList.get(0).getSurveyId());
        assertEquals(surveyName, surveyStatsList.get(0).getSurveyName());
        assertEquals(1, surveyStatsList.get(0).getCompletes());
        assertEquals(0, surveyStatsList.get(0).getFilteredParticipants());
        assertEquals(0, surveyStatsList.get(0).getRejectedParticipants());
        assertEquals(length.doubleValue(), surveyStatsList.get(0).getAverageLength());
    }
    @Test
    void testGetSurveysWithStatsReturnsCorrectStatsForFilteredParticipants() {
        //Given
        Long surveyId = 1L;
        Survey survey = mock(Survey.class);
        String surveyName = "survey";
        Participation participation = mock(Participation.class);

        when(surveyRepository.findAll()).thenReturn(List.of(survey));

        when(survey.getId()).thenReturn(surveyId);
        when(survey.getName()).thenReturn(surveyName);

        when(participationRepository.findAllBySurvey_Id(surveyId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.FILTERED);
        when(participation.getLength()).thenReturn(Optional.empty());

        //When
        List<SurveyStats> surveyStatsList = serviceUnderTest.getSurveysWithStats();

        //Then
        assertNotNull(surveyStatsList);
        assertEquals(1, surveyStatsList.size());
        assertEquals(surveyId, surveyStatsList.get(0).getSurveyId());
        assertEquals(surveyName, surveyStatsList.get(0).getSurveyName());
        assertEquals(0, surveyStatsList.get(0).getCompletes());
        assertEquals(1, surveyStatsList.get(0).getFilteredParticipants());
        assertEquals(0, surveyStatsList.get(0).getRejectedParticipants());
        assertEquals(0.0, surveyStatsList.get(0).getAverageLength());
    }

    @Test
    void testGetSurveysWithStatsReturnsCorrectStatsForRejectedParticipants() {
        //Given
        Long surveyId = 1L;
        Survey survey = mock(Survey.class);
        String surveyName = "survey";
        Participation participation = mock(Participation.class);

        when(surveyRepository.findAll()).thenReturn(List.of(survey));

        when(survey.getId()).thenReturn(surveyId);
        when(survey.getName()).thenReturn(surveyName);

        when(participationRepository.findAllBySurvey_Id(surveyId)).thenReturn(List.of(participation));

        when(participation.getStatus()).thenReturn(Status.REJECTED);
        when(participation.getLength()).thenReturn(Optional.empty());

        //When
        List<SurveyStats> surveyStatsList = serviceUnderTest.getSurveysWithStats();

        //Then
        assertNotNull(surveyStatsList);
        assertEquals(1, surveyStatsList.size());
        assertEquals(surveyId, surveyStatsList.get(0).getSurveyId());
        assertEquals(surveyName, surveyStatsList.get(0).getSurveyName());
        assertEquals(0, surveyStatsList.get(0).getCompletes());
        assertEquals(0, surveyStatsList.get(0).getFilteredParticipants());
        assertEquals(1, surveyStatsList.get(0).getRejectedParticipants());
        assertEquals(0.0, surveyStatsList.get(0).getAverageLength());
    }

    @Test
    void testGetSurveysWithStatsReturnsCorrectAverageLength() {
        //Given
        Long surveyId = 1L;
        Survey survey = mock(Survey.class);
        String surveyName = "survey";
        Participation participation1 = mock(Participation.class);
        Participation participation2 = mock(Participation.class);
        Integer length1 = 10;
        Integer length2 = 20;

        when(surveyRepository.findAll()).thenReturn(List.of(survey));

        when(survey.getId()).thenReturn(surveyId);
        when(survey.getName()).thenReturn(surveyName);

        when(participationRepository.findAllBySurvey_Id(surveyId)).thenReturn(List.of(participation1, participation2));

        when(participation1.getStatus()).thenReturn(Status.COMPLETED);
        when(participation1.getLength()).thenReturn(Optional.of(length1));

        when(participation2.getStatus()).thenReturn(Status.COMPLETED);
        when(participation2.getLength()).thenReturn(Optional.of(length2));

        //When
        List<SurveyStats> surveyStatsList = serviceUnderTest.getSurveysWithStats();

        //Then
        assertNotNull(surveyStatsList);
        assertEquals(1, surveyStatsList.size());
        assertEquals(surveyId, surveyStatsList.get(0).getSurveyId());
        assertEquals(surveyName, surveyStatsList.get(0).getSurveyName());
        assertEquals(2, surveyStatsList.get(0).getCompletes());
        assertEquals(0, surveyStatsList.get(0).getFilteredParticipants());
        assertEquals(0, surveyStatsList.get(0).getRejectedParticipants());
        assertEquals((length1 + length2) / 2.0, surveyStatsList.get(0).getAverageLength());
    }

    @Test
    void testGetSurveysWithStatsReturnsCorrectAverageLengthWhenOneIsEmpty() {
        //Given
        Long surveyId = 1L;
        Survey survey = mock(Survey.class);
        String surveyName = "survey";
        Participation participation1 = mock(Participation.class);
        Participation participation2 = mock(Participation.class);
        Integer length = 10;

        when(surveyRepository.findAll()).thenReturn(List.of(survey));

        when(survey.getId()).thenReturn(surveyId);
        when(survey.getName()).thenReturn(surveyName);

        when(participationRepository.findAllBySurvey_Id(surveyId)).thenReturn(List.of(participation1, participation2));

        when(participation1.getStatus()).thenReturn(Status.COMPLETED);
        when(participation1.getLength()).thenReturn(Optional.of(length));

        when(participation2.getStatus()).thenReturn(Status.REJECTED);
        when(participation2.getLength()).thenReturn(Optional.empty());

        //When
        List<SurveyStats> surveyStatsList = serviceUnderTest.getSurveysWithStats();

        //Then
        assertNotNull(surveyStatsList);
        assertEquals(1, surveyStatsList.size());
        assertEquals(surveyId, surveyStatsList.get(0).getSurveyId());
        assertEquals(surveyName, surveyStatsList.get(0).getSurveyName());
        assertEquals(1, surveyStatsList.get(0).getCompletes());
        assertEquals(0, surveyStatsList.get(0).getFilteredParticipants());
        assertEquals(1, surveyStatsList.get(0).getRejectedParticipants());
        assertEquals(length.doubleValue(), surveyStatsList.get(0).getAverageLength());
    }
}