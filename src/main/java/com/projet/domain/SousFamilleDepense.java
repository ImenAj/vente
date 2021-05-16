package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SousFamilleDepense.
 */
@Entity
@Table(name = "sous_famille_depense")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sousfamilledepense")
public class SousFamilleDepense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @ManyToOne(optional = false)
    @NotNull
    private FamilleDepense familleDepense;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public SousFamilleDepense designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public FamilleDepense getFamilleDepense() {
        return familleDepense;
    }

    public SousFamilleDepense familleDepense(FamilleDepense familleDepense) {
        this.familleDepense = familleDepense;
        return this;
    }

    public void setFamilleDepense(FamilleDepense familleDepense) {
        this.familleDepense = familleDepense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SousFamilleDepense sousFamilleDepense = (SousFamilleDepense) o;
        if (sousFamilleDepense.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sousFamilleDepense.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SousFamilleDepense{" +
            "id=" + id +
            ", designation='" + designation + "'" +
            '}';
    }
}
