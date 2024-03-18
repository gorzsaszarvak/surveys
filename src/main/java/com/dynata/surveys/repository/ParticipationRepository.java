package com.dynata.surveys.repository;

import com.dynata.surveys.domain.Participation;
import com.dynata.surveys.domain.ParticipationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, ParticipationKey> {
    List<Participation> findAllByMember_Id(Long memberId);
    List<Participation> findAllBySurvey_Id(Long surveyId);
}
