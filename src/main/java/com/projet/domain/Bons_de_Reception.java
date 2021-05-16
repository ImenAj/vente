package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Bons_de_Reception.
 */
@Entity
@Table(name = "bons_de_reception")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bons_de_reception")
public class Bons_de_Reception implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_reception", nullable = false)
    private LocalDate dateReception;

    @Column(name = "objet")
    private String objet;

    @ManyToOne(optional = false)
    @NotNull
    private Fournisseur fournisseur;

    @ManyToOne(optional = false)
    @NotNull
    private Condition_de_reglement condition_de_reglement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateReception() {
        return dateReception;
    }

    public Bons_de_Reception dateReception(LocalDate dateReception) {
        this.dateReception = dateReception;
        return this;
    }

    public void setDateReception(LocalDate dateReception) {
        this.dateReception = dateReception;
    }

    public String getObjet() {
        return objet;
    }

    public Bons_de_Reception objet(String objet) {
        this.objet = objet;
        return this;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public Bons_de_Reception fournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        return this;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Condition_de_reglement getCondition_de_reglement() {
        return condition_de_reglement;
    }

    public Bons_de_Reception condition_de_reglement(Condition_de_reglement condition_de_reglement) {
        this.condition_de_reglement = condition_de_reglement;
        return this;
    }

    public void setCondition_de_reglement(Condition_de_reglement condition_de_reglement) {
        this.condition_de_reglement = condition_de_reglement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bons_de_Reception bons_de_Reception = (Bons_de_Reception) o;
        if (bons_de_Reception.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bons_de_Reception.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bons_de_Reception{" +
            "id=" + id +
            ", dateReception='" + dateReception + "'" +
            ", objet='" + objet + "'" +
            '}';
    }
}
