package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "client")
public class Client implements Serializable {

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
    @Column(name = "adress", nullable = false)
    private String adress;

    @NotNull
    @Column(name = "code_postal", nullable = false)
    private Integer code_postal;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    @Column(name = "tel")
    private Integer tel;

    @Column(name = "fax")
    private Integer fax;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Client nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Client prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdress() {
        return adress;
    }

    public Client adress(String adress) {
        this.adress = adress;
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Integer getCode_postal() {
        return code_postal;
    }

    public Client code_postal(Integer code_postal) {
        this.code_postal = code_postal;
        return this;
    }

    public void setCode_postal(Integer code_postal) {
        this.code_postal = code_postal;
    }

    public String getVille() {
        return ville;
    }

    public Client ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getTel() {
        return tel;
    }

    public Client tel(Integer tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }

    public Integer getFax() {
        return fax;
    }

    public Client fax(Integer fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(Integer fax) {
        this.fax = fax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        if (client.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", adress='" + adress + "'" +
            ", code_postal='" + code_postal + "'" +
            ", ville='" + ville + "'" +
            ", tel='" + tel + "'" +
            ", fax='" + fax + "'" +
            '}';
    }
}
