package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Fournisseur.
 */
@Entity
@Table(name = "fournisseur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fournisseur")
public class Fournisseur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "matricule_fiscal", nullable = false)
    private String matricule_fiscal;

    @NotNull
    @Column(name = "raison_social", nullable = false)
    private String raison_social;

    @NotNull
    @Column(name = "adress", nullable = false)
    private String adress;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    @NotNull
    @Column(name = "code_postal", nullable = false)
    private Integer code_postal;

    @NotNull
    @Column(name = "tel", nullable = false)
    private Integer tel;

    @Column(name = "fax")
    private Integer fax;

    @Column(name = "site_web")
    private String site_web;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Fournisseur nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Fournisseur prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMatricule_fiscal() {
        return matricule_fiscal;
    }

    public Fournisseur matricule_fiscal(String matricule_fiscal) {
        this.matricule_fiscal = matricule_fiscal;
        return this;
    }

    public void setMatricule_fiscal(String matricule_fiscal) {
        this.matricule_fiscal = matricule_fiscal;
    }

    public String getRaison_social() {
        return raison_social;
    }

    public Fournisseur raison_social(String raison_social) {
        this.raison_social = raison_social;
        return this;
    }

    public void setRaison_social(String raison_social) {
        this.raison_social = raison_social;
    }

    public String getAdress() {
        return adress;
    }

    public Fournisseur adress(String adress) {
        this.adress = adress;
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getVille() {
        return ville;
    }

    public Fournisseur ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getCode_postal() {
        return code_postal;
    }

    public Fournisseur code_postal(Integer code_postal) {
        this.code_postal = code_postal;
        return this;
    }

    public void setCode_postal(Integer code_postal) {
        this.code_postal = code_postal;
    }

    public Integer getTel() {
        return tel;
    }

    public Fournisseur tel(Integer tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }

    public Integer getFax() {
        return fax;
    }

    public Fournisseur fax(Integer fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(Integer fax) {
        this.fax = fax;
    }

    public String getSite_web() {
        return site_web;
    }

    public Fournisseur site_web(String site_web) {
        this.site_web = site_web;
        return this;
    }

    public void setSite_web(String site_web) {
        this.site_web = site_web;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fournisseur fournisseur = (Fournisseur) o;
        if (fournisseur.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fournisseur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", matricule_fiscal='" + matricule_fiscal + "'" +
            ", raison_social='" + raison_social + "'" +
            ", adress='" + adress + "'" +
            ", ville='" + ville + "'" +
            ", code_postal='" + code_postal + "'" +
            ", tel='" + tel + "'" +
            ", fax='" + fax + "'" +
            ", site_web='" + site_web + "'" +
            '}';
    }
}
