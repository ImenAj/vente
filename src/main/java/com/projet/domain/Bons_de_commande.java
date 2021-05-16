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
 * A Bons_de_commande.
 */
@Entity
@Table(name = "bons_de_commande")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bons_de_commande")
public class Bons_de_commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "referance", nullable = false)
    private String referance;

    @Column(name = "etat")
    private String etat;

    @Column(name = "site")
    private String site;

    @NotNull
    @Column(name = "date_commande_e", nullable = false)
    private LocalDate date_commandeE;

    @NotNull
    @Column(name = "date_livraison_prevue", nullable = false)
    private LocalDate date_livraison_prevue;

    @NotNull
    @Column(name = "livre", nullable = false)
    private Boolean livre;

    @ManyToOne(optional = false)
    @NotNull
    private Fournisseur fournisseur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferance() {
        return referance;
    }

    public Bons_de_commande referance(String referance) {
        this.referance = referance;
        return this;
    }

    public void setReferance(String referance) {
        this.referance = referance;
    }

    public String getEtat() {
        return etat;
    }

    public Bons_de_commande etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getSite() {
        return site;
    }

    public Bons_de_commande site(String site) {
        this.site = site;
        return this;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public LocalDate getDate_commandeE() {
        return date_commandeE;
    }

    public Bons_de_commande date_commandeE(LocalDate date_commandeE) {
        this.date_commandeE = date_commandeE;
        return this;
    }

    public void setDate_commandeE(LocalDate date_commandeE) {
        this.date_commandeE = date_commandeE;
    }

    public LocalDate getDate_livraison_prevue() {
        return date_livraison_prevue;
    }

    public Bons_de_commande date_livraison_prevue(LocalDate date_livraison_prevue) {
        this.date_livraison_prevue = date_livraison_prevue;
        return this;
    }

    public void setDate_livraison_prevue(LocalDate date_livraison_prevue) {
        this.date_livraison_prevue = date_livraison_prevue;
    }

    public Boolean isLivre() {
        return livre;
    }

    public Bons_de_commande livre(Boolean livre) {
        this.livre = livre;
        return this;
    }

    public void setLivre(Boolean livre) {
        this.livre = livre;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public Bons_de_commande fournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        return this;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bons_de_commande bons_de_commande = (Bons_de_commande) o;
        if (bons_de_commande.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bons_de_commande.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bons_de_commande{" +
            "id=" + id +
            ", referance='" + referance + "'" +
            ", etat='" + etat + "'" +
            ", site='" + site + "'" +
            ", date_commandeE='" + date_commandeE + "'" +
            ", date_livraison_prevue='" + date_livraison_prevue + "'" +
            ", livre='" + livre + "'" +
            '}';
    }
}
