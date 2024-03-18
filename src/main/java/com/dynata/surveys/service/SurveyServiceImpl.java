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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final ParticipationRepository participationRepository;

    @Override
    public List<MemberDto> getRespondentsForSurvey(Long surveyId) {
        return participationRepository.findAllBySurvey_Id(surveyId).stream()
                .filter(participation -> participation.getStatus().equals(Status.COMPLETED))
                .map(Participation::getMember)
                .map(this::mapToMemberDto)
                .toList();
    }

    @Override
    public List<SurveyDto> getCompletedSurveysForMember(Long memberId) {
        return participationRepository.findAllByMember_Id(memberId).stream()
                .filter(participation -> participation.getStatus().equals(Status.COMPLETED))
                .map(Participation::getSurvey)
                .map(this::mapToSurveyDto)
                .toList();
    }

    @Override
    public Map<Long, Integer> getPointsForMember(Long memberId) {
        Map<Long, Integer> pointsBySurvey = new HashMap<>();
        participationRepository.findAllByMember_Id(memberId).stream()
                .filter(participation ->
                        participation.getStatus().equals(Status.COMPLETED) || participation.getStatus().equals(Status.FILTERED))
                .forEach(participation -> {
                    Survey survey = participation.getSurvey();
                    Integer points = (participation.getStatus()
                            .equals(Status.COMPLETED)) ? survey.getCompletionPoints() : survey.getFilteredPoints();

                    pointsBySurvey.put(survey.getId(), points);
                });

        return pointsBySurvey;
    }

    @Override
    public List<MemberDto> getEligibleMembersForSurvey(Long surveyId) {
        List<MemberDto> eligibleMembers = new ArrayList<>();
        memberRepository.findAll().forEach(member -> {
            if (member.isActive() && !hasParticipated(member.getId(), surveyId))
                eligibleMembers.add(mapToMemberDto(member));
        });

        return eligibleMembers;
    }

    @Override
    public List<SurveyStats> getSurveysWithStats() {
        return surveyRepository.findAll().stream().map(this::createStatsFromSurvey).toList();
    }

    private MemberDto mapToMemberDto(Member member) {
        return new MemberDto(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.isActive()
        );
    }

    private SurveyDto mapToSurveyDto(Survey survey) {
        return new SurveyDto(
                survey.getId(),
                survey.getName(),
                survey.getExpectedCompletes(),
                survey.getCompletionPoints(),
                survey.getFilteredPoints()
        );
    }

    private boolean hasParticipated(Long memberId, Long surveyId) {
        return participationRepository.findAllBySurvey_Id(surveyId).stream()
                .anyMatch(participation -> participation.getMember().getId().equals(memberId));
    }

    private SurveyStats createStatsFromSurvey(Survey survey) {
        List<Participation> participations = participationRepository.findAllBySurvey_Id(survey.getId());
        int completes = countParticipantsByStatus(participations, Status.COMPLETED);
        int filteredParticipants = countParticipantsByStatus(participations, Status.FILTERED);
        int rejectedParticipants = countParticipantsByStatus(participations, Status.REJECTED);


        return SurveyStats.builder()
                .surveyId(survey.getId())
                .surveyName(survey.getName())
                .completes(completes)
                .filteredParticipants(filteredParticipants)
                .rejectedParticipants(rejectedParticipants)
                .averageLength(getAverageLength(participations))
                .build();
    }

    private int countParticipantsByStatus(List<Participation> participations, Status status) {
        return (int) participations.stream()
                .filter(participation -> participation.getStatus().equals(status))
                .count();
    }

    private double getAverageLength(List<Participation> participations) {
        return participations.stream()
                .filter(participation -> participation.getLength().isPresent())
                .mapToDouble(participation -> participation.getLength().get())
                .average()
                .orElse(0.0);
    }
}
