package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FamilleDepense.
 */
@Entity
@Table(name = "famille_depense")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "familledepense")
public class FamilleDepense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "designation_famille", nullable = false)
    private String designationFamille;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignationFamille() {
        return designationFamille;
    }

    public FamilleDepense designationFamille(String designationFamille) {
        this.designationFamille = designationFamille;
        return this;
    }

    public void setDesignationFamille(String designationFamille) {
        this.designationFamille = designationFamille;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FamilleDepense familleDepense = (FamilleDepense) o;
        if (familleDepense.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, familleDepense.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FamilleDepense{" +
            "id=" + id +
            ", designationFamille='" + designationFamille + "'" +
            '}';
    }
}
