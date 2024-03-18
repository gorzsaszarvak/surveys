package com.dynata.surveys.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer expectedCompletes;
    private Integer completionPoints;
    private Integer filteredPoints;
    @OneToMany(mappedBy = "survey")
    private Set<Participation> participations;
}
