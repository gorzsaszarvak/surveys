package com.dynata.surveys.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@IdClass(ParticipationKey.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participation {
    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Id
    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;
    private Status status;
    @Column(nullable = true)
    private Integer length;

    public Optional<Integer> getLength() {
        return Optional.ofNullable(length);
    }
}
