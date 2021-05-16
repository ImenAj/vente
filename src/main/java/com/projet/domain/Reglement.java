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
 * A Reglement.
 */
@Entity
@Table(name = "reglement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reglement")
public class Reglement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_reglement", nullable = false)
    private LocalDate date_Reglement;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "montant", nullable = false)
    private Double montant;

    @ManyToOne(optional = false)
    @NotNull
    private Depense depense;

    @ManyToOne(optional = false)
    @NotNull
    private Mode_de_paiment mode_de_paiment;

    @ManyToOne(optional = false)
    @NotNull
    private Condition_de_reglement condition_de_reglement;

    @ManyToOne(optional = false)
    @NotNull
    private Article article;

    @ManyToOne(optional = false)
    @NotNull
    private Facture facture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate_Reglement() {
        return date_Reglement;
    }

    public Reglement date_Reglement(LocalDate date_Reglement) {
        this.date_Reglement = date_Reglement;
        return this;
    }

    public void setDate_Reglement(LocalDate date_Reglement) {
        this.date_Reglement = date_Reglement;
    }

    public String getLibelle() {
        return libelle;
    }

    public Reglement libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getMontant() {
        return montant;
    }

    public Reglement montant(Double montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Depense getDepense() {
        return depense;
    }

    public Reglement depense(Depense depense) {
        this.depense = depense;
        return this;
    }

    public void setDepense(Depense depense) {
        this.depense = depense;
    }

    public Mode_de_paiment getMode_de_paiment() {
        return mode_de_paiment;
    }

    public Reglement mode_de_paiment(Mode_de_paiment mode_de_paiment) {
        this.mode_de_paiment = mode_de_paiment;
        return this;
    }

    public void setMode_de_paiment(Mode_de_paiment mode_de_paiment) {
        this.mode_de_paiment = mode_de_paiment;
    }

    public Condition_de_reglement getCondition_de_reglement() {
        return condition_de_reglement;
    }

    public Reglement condition_de_reglement(Condition_de_reglement condition_de_reglement) {
        this.condition_de_reglement = condition_de_reglement;
        return this;
    }

    public void setCondition_de_reglement(Condition_de_reglement condition_de_reglement) {
        this.condition_de_reglement = condition_de_reglement;
    }

    public Article getArticle() {
        return article;
    }

    public Reglement article(Article article) {
        this.article = article;
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Facture getFacture() {
        return facture;
    }

    public Reglement facture(Facture facture) {
        this.facture = facture;
        return this;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reglement reglement = (Reglement) o;
        if (reglement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reglement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Reglement{" +
            "id=" + id +
            ", date_Reglement='" + date_Reglement + "'" +
            ", libelle='" + libelle + "'" +
            ", montant='" + montant + "'" +
            '}';
    }
}
