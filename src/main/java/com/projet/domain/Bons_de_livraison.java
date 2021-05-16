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
 * A Bons_de_livraison.
 */
@Entity
@Table(name = "bons_de_livraison")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bons_de_livraison")
public class Bons_de_livraison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @NotNull
    @Column(name = "quantite_commandees", nullable = false)
    private Integer quantite_commandees;

    @NotNull
    @Column(name = "date_livraison", nullable = false)
    private LocalDate date_livraison;

    @Column(name = "objet")
    private String objet;

    @Lob
    @Column(name = "piece_jointe")
    private byte[] piece_jointe;

    @Column(name = "piece_jointe_content_type")
    private String piece_jointeContentType;

    @Column(name = "reference")
    private String reference;

    @ManyToOne(optional = false)
    @NotNull
    private Article article;

    @ManyToOne
    private Commande commande;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public Bons_de_livraison designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getQuantite_commandees() {
        return quantite_commandees;
    }

    public Bons_de_livraison quantite_commandees(Integer quantite_commandees) {
        this.quantite_commandees = quantite_commandees;
        return this;
    }

    public void setQuantite_commandees(Integer quantite_commandees) {
        this.quantite_commandees = quantite_commandees;
    }

    public LocalDate getDate_livraison() {
        return date_livraison;
    }

    public Bons_de_livraison date_livraison(LocalDate date_livraison) {
        this.date_livraison = date_livraison;
        return this;
    }

    public void setDate_livraison(LocalDate date_livraison) {
        this.date_livraison = date_livraison;
    }

    public String getObjet() {
        return objet;
    }

    public Bons_de_livraison objet(String objet) {
        this.objet = objet;
        return this;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public byte[] getPiece_jointe() {
        return piece_jointe;
    }

    public Bons_de_livraison piece_jointe(byte[] piece_jointe) {
        this.piece_jointe = piece_jointe;
        return this;
    }

    public void setPiece_jointe(byte[] piece_jointe) {
        this.piece_jointe = piece_jointe;
    }

    public String getPiece_jointeContentType() {
        return piece_jointeContentType;
    }

    public Bons_de_livraison piece_jointeContentType(String piece_jointeContentType) {
        this.piece_jointeContentType = piece_jointeContentType;
        return this;
    }

    public void setPiece_jointeContentType(String piece_jointeContentType) {
        this.piece_jointeContentType = piece_jointeContentType;
    }

    public String getReference() {
        return reference;
    }

    public Bons_de_livraison reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Article getArticle() {
        return article;
    }

    public Bons_de_livraison article(Article article) {
        this.article = article;
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Commande getCommande() {
        return commande;
    }

    public Bons_de_livraison commande(Commande commande) {
        this.commande = commande;
        return this;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bons_de_livraison bons_de_livraison = (Bons_de_livraison) o;
        if (bons_de_livraison.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bons_de_livraison.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bons_de_livraison{" +
            "id=" + id +
            ", designation='" + designation + "'" +
            ", quantite_commandees='" + quantite_commandees + "'" +
            ", date_livraison='" + date_livraison + "'" +
            ", objet='" + objet + "'" +
            ", piece_jointe='" + piece_jointe + "'" +
            ", piece_jointeContentType='" + piece_jointeContentType + "'" +
            ", reference='" + reference + "'" +
            '}';
    }
}
