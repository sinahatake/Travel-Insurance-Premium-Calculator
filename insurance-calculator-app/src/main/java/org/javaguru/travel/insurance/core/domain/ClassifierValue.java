package org.javaguru.travel.insurance.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "classifier_values")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassifierValue {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "classifier_id", nullable = false)
    private Classifier classifier;

    @Column(name = "ic", nullable = false)
    private String ic;

    @Column(name = "description", nullable = false)
    private String description;

}