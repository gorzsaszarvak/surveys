package com.dynata.surveys.repository;

import com.dynata.surveys.domain.Member;
import com.dynata.surveys.domain.Participation;
import com.dynata.surveys.domain.Status;
import com.dynata.surveys.domain.Survey;
import com.dynata.surveys.repository.exception.MemberNotFoundException;
import com.dynata.surveys.repository.exception.SurveyNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoadData {
    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final ParticipationRepository participationRepository;
    @Value("${members.path}")
    private String membersPath;
    @Value("${surveys.path}")
    private String surveysPath;
    @Value("${participation.path}")
    private String participationsPath;

    @PostConstruct
    private void initialize() {
        loadMembers();
        loadSurveys();
        loadParticipations();
    }

    private void loadMembers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(membersPath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                Member member = Member.builder()
                        .id(Long.parseLong(fields[0]))
                        .name(fields[1])
                        .email(fields[2])
                        .isActive(Integer.parseInt(fields[3]) == 1)
                        .build();
                memberRepository.save(member);
            }
        } catch (IOException e) {
            log.error("Error during initializing members: ", e);
        }
    }

    private void loadSurveys() {
        try (BufferedReader reader = new BufferedReader(new FileReader(surveysPath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                Survey survey = Survey.builder()
                        .id(Long.parseLong(fields[0]))
                        .name(fields[1])
                        .expectedCompletes(Integer.parseInt(fields[2]))
                        .completionPoints(Integer.parseInt(fields[3]))
                        .filteredPoints(Integer.parseInt(fields[4]))
                        .build();
                surveyRepository.save(survey);
            }
        } catch (IOException e) {
            log.error("Error during initializing surveys: ", e);
        }
    }

    private void loadParticipations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(participationsPath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                Participation participation = Participation.builder()
                        .member(memberRepository.findById(Long.parseLong(fields[0])).orElseThrow(() -> new MemberNotFoundException(Long.parseLong(fields[0]))))
                        .survey(surveyRepository.findById(Long.parseLong(fields[1])).orElseThrow(() -> new SurveyNotFoundException(Long.parseLong(fields[1]))))
                        .status(Status.fromInt(Integer.parseInt(fields[2])))
                        .length((fields.length > 3) ? Integer.parseInt(fields[3]) : null)
                        .build();
                participationRepository.save(participation);
            }
        } catch (Exception e) {
            log.error("Error during initializing participations: ", e);
        }
    }
}
