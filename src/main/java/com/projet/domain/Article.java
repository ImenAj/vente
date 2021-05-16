package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "referance", nullable = false)
    private String referance;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @NotNull
    @Column(name = "prix_achat", nullable = false)
    private Double prix_achat;

    @NotNull
    @Column(name = "unite", nullable = false)
    private Integer unite;

    @NotNull
    @Column(name = "prix_vente", nullable = false)
    private Double prix_vente;

    @Lob
    @Column(name = "document")
    private byte[] document;

    @Column(name = "document_content_type")
    private String documentContentType;

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @ManyToOne
    private Sous_famille sous_famille;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferance() {
        return referance;
    }

    public Article referance(String referance) {
        this.referance = referance;
        return this;
    }

    public void setReferance(String referance) {
        this.referance = referance;
    }

    public String getDesignation() {
        return designation;
    }

    public Article designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getPrix_achat() {
        return prix_achat;
    }

    public Article prix_achat(Double prix_achat) {
        this.prix_achat = prix_achat;
        return this;
    }

    public void setPrix_achat(Double prix_achat) {
        this.prix_achat = prix_achat;
    }

    public Integer getUnite() {
        return unite;
    }

    public Article unite(Integer unite) {
        this.unite = unite;
        return this;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public Double getPrix_vente() {
        return prix_vente;
    }

    public Article prix_vente(Double prix_vente) {
        this.prix_vente = prix_vente;
        return this;
    }

    public void setPrix_vente(Double prix_vente) {
        this.prix_vente = prix_vente;
    }

    public byte[] getDocument() {
        return document;
    }

    public Article document(byte[] document) {
        this.document = document;
        return this;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public Article documentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
        return this;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public Article quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Sous_famille getSous_famille() {
        return sous_famille;
    }

    public Article sous_famille(Sous_famille sous_famille) {
        this.sous_famille = sous_famille;
        return this;
    }

    public void setSous_famille(Sous_famille sous_famille) {
        this.sous_famille = sous_famille;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Article article = (Article) o;
        if (article.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + id +
            ", referance='" + referance + "'" +
            ", designation='" + designation + "'" +
            ", prix_achat='" + prix_achat + "'" +
            ", unite='" + unite + "'" +
            ", prix_vente='" + prix_vente + "'" +
            ", document='" + document + "'" +
            ", documentContentType='" + documentContentType + "'" +
            ", quantite='" + quantite + "'" +
            '}';
    }
}
