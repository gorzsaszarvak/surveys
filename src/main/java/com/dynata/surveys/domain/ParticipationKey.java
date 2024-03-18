package com.dynata.surveys.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParticipationKey implements Serializable {
    private Long member;
    private Long survey;
}
