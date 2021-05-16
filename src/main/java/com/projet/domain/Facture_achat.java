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
 * A Facture_achat.
 */
@Entity
@Table(name = "facture_achat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "facture_achat")
public class Facture_achat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "referance", nullable = false)
    private String referance;

    @NotNull
    @Column(name = "date_facture", nullable = false)
    private LocalDate date_facture;

    @NotNull
    @Column(name = "echeance", nullable = false)
    private LocalDate echeance;

    @NotNull
    @Column(name = "montant", nullable = false)
    private Double montant;

    @NotNull
    @Lob
    @Column(name = "piece_jointe", nullable = false)
    private byte[] piece_jointe;

    @Column(name = "piece_jointe_content_type", nullable = false)
    private String piece_jointeContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferance() {
        return referance;
    }

    public Facture_achat referance(String referance) {
        this.referance = referance;
        return this;
    }

    public void setReferance(String referance) {
        this.referance = referance;
    }

    public LocalDate getDate_facture() {
        return date_facture;
    }

    public Facture_achat date_facture(LocalDate date_facture) {
        this.date_facture = date_facture;
        return this;
    }

    public void setDate_facture(LocalDate date_facture) {
        this.date_facture = date_facture;
    }

    public LocalDate getEcheance() {
        return echeance;
    }

    public Facture_achat echeance(LocalDate echeance) {
        this.echeance = echeance;
        return this;
    }

    public void setEcheance(LocalDate echeance) {
        this.echeance = echeance;
    }

    public Double getMontant() {
        return montant;
    }

    public Facture_achat montant(Double montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public byte[] getPiece_jointe() {
        return piece_jointe;
    }

    public Facture_achat piece_jointe(byte[] piece_jointe) {
        this.piece_jointe = piece_jointe;
        return this;
    }

    public void setPiece_jointe(byte[] piece_jointe) {
        this.piece_jointe = piece_jointe;
    }

    public String getPiece_jointeContentType() {
        return piece_jointeContentType;
    }

    public Facture_achat piece_jointeContentType(String piece_jointeContentType) {
        this.piece_jointeContentType = piece_jointeContentType;
        return this;
    }

    public void setPiece_jointeContentType(String piece_jointeContentType) {
        this.piece_jointeContentType = piece_jointeContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Facture_achat facture_achat = (Facture_achat) o;
        if (facture_achat.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, facture_achat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Facture_achat{" +
            "id=" + id +
            ", referance='" + referance + "'" +
            ", date_facture='" + date_facture + "'" +
            ", echeance='" + echeance + "'" +
            ", montant='" + montant + "'" +
            ", piece_jointe='" + piece_jointe + "'" +
            ", piece_jointeContentType='" + piece_jointeContentType + "'" +
            '}';
    }
}
