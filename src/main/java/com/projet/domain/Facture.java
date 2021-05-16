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
 * A Facture.
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "facture")
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "numero_facture", nullable = false)
    private Integer numeroFacture;

    @NotNull
    @Column(name = "date_facture", nullable = false)
    private LocalDate dateFacture;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @NotNull
    @Column(name = "prix_uht", nullable = false)
    private Double prix_UHT;

    @NotNull
    @Column(name = "referance", nullable = false)
    private String referance;

    @NotNull
    @Lob
    @Column(name = "piece_jointe", nullable = false)
    private byte[] piece_jointe;

    @Column(name = "piece_jointe_content_type", nullable = false)
    private String piece_jointeContentType;

    @Column(name = "sortie")
    private Boolean sortie;

    @ManyToOne(optional = false)
    @NotNull
    private Client client;

    @ManyToOne
    private Bons_de_livraison bons_de_livraison;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroFacture() {
        return numeroFacture;
    }

    public Facture numeroFacture(Integer numeroFacture) {
        this.numeroFacture = numeroFacture;
        return this;
    }

    public void setNumeroFacture(Integer numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public Facture dateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
        return this;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public String getDesignation() {
        return designation;
    }

    public Facture designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public Facture quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPrix_UHT() {
        return prix_UHT;
    }

    public Facture prix_UHT(Double prix_UHT) {
        this.prix_UHT = prix_UHT;
        return this;
    }

    public void setPrix_UHT(Double prix_UHT) {
        this.prix_UHT = prix_UHT;
    }

    public String getReferance() {
        return referance;
    }

    public Facture referance(String referance) {
        this.referance = referance;
        return this;
    }

    public void setReferance(String referance) {
        this.referance = referance;
    }

    public byte[] getPiece_jointe() {
        return piece_jointe;
    }

    public Facture piece_jointe(byte[] piece_jointe) {
        this.piece_jointe = piece_jointe;
        return this;
    }

    public void setPiece_jointe(byte[] piece_jointe) {
        this.piece_jointe = piece_jointe;
    }

    public String getPiece_jointeContentType() {
        return piece_jointeContentType;
    }

    public Facture piece_jointeContentType(String piece_jointeContentType) {
        this.piece_jointeContentType = piece_jointeContentType;
        return this;
    }

    public void setPiece_jointeContentType(String piece_jointeContentType) {
        this.piece_jointeContentType = piece_jointeContentType;
    }

    public Boolean isSortie() {
        return sortie;
    }

    public Facture sortie(Boolean sortie) {
        this.sortie = sortie;
        return this;
    }

    public void setSortie(Boolean sortie) {
        this.sortie = sortie;
    }

    public Client getClient() {
        return client;
    }

    public Facture client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Bons_de_livraison getBons_de_livraison() {
        return bons_de_livraison;
    }

    public Facture bons_de_livraison(Bons_de_livraison bons_de_livraison) {
        this.bons_de_livraison = bons_de_livraison;
        return this;
    }

    public void setBons_de_livraison(Bons_de_livraison bons_de_livraison) {
        this.bons_de_livraison = bons_de_livraison;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Facture facture = (Facture) o;
        if (facture.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, facture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Facture{" +
            "id=" + id +
            ", numeroFacture='" + numeroFacture + "'" +
            ", dateFacture='" + dateFacture + "'" +
            ", designation='" + designation + "'" +
            ", quantite='" + quantite + "'" +
            ", prix_UHT='" + prix_UHT + "'" +
            ", referance='" + referance + "'" +
            ", piece_jointe='" + piece_jointe + "'" +
            ", piece_jointeContentType='" + piece_jointeContentType + "'" +
            ", sortie='" + sortie + "'" +
            '}';
    }
}
