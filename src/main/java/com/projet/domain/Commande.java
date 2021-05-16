package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Commande.
 */
@Entity
@Table(name = "commande")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "commande")
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_commande", nullable = false)
    private ZonedDateTime date_commande;

    @Column(name = "designation")
    private String designation;

    @Lob
    @Column(name = "piece_jointe")
    private byte[] piece_jointe;

    @Column(name = "piece_jointe_content_type")
    private String piece_jointeContentType;

    @Column(name = "reference")
    private String reference;

    @ManyToOne
    private Mode_de_paiment mode_de_paiment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate_commande() {
        return date_commande;
    }

    public Commande date_commande(ZonedDateTime date_commande) {
        this.date_commande = date_commande;
        return this;
    }

    public void setDate_commande(ZonedDateTime date_commande) {
        this.date_commande = date_commande;
    }

    public String getDesignation() {
        return designation;
    }

    public Commande designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public byte[] getPiece_jointe() {
        return piece_jointe;
    }

    public Commande piece_jointe(byte[] piece_jointe) {
        this.piece_jointe = piece_jointe;
        return this;
    }

    public void setPiece_jointe(byte[] piece_jointe) {
        this.piece_jointe = piece_jointe;
    }

    public String getPiece_jointeContentType() {
        return piece_jointeContentType;
    }

    public Commande piece_jointeContentType(String piece_jointeContentType) {
        this.piece_jointeContentType = piece_jointeContentType;
        return this;
    }

    public void setPiece_jointeContentType(String piece_jointeContentType) {
        this.piece_jointeContentType = piece_jointeContentType;
    }

    public String getReference() {
        return reference;
    }

    public Commande reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Mode_de_paiment getMode_de_paiment() {
        return mode_de_paiment;
    }

    public Commande mode_de_paiment(Mode_de_paiment mode_de_paiment) {
        this.mode_de_paiment = mode_de_paiment;
        return this;
    }

    public void setMode_de_paiment(Mode_de_paiment mode_de_paiment) {
        this.mode_de_paiment = mode_de_paiment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Commande commande = (Commande) o;
        if (commande.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, commande.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Commande{" +
            "id=" + id +
            ", date_commande='" + date_commande + "'" +
            ", designation='" + designation + "'" +
            ", piece_jointe='" + piece_jointe + "'" +
            ", piece_jointeContentType='" + piece_jointeContentType + "'" +
            ", reference='" + reference + "'" +
            '}';
    }
}
