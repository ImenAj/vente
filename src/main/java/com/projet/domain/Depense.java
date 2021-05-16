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
 * A Depense.
 */
@Entity
@Table(name = "depense")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "depense")
public class Depense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_depense", nullable = false)
    private LocalDate date_depense;

    @NotNull
    @Column(name = "date_echeance", nullable = false)
    private LocalDate date_echeance;

    @NotNull
    @Column(name = "reference", nullable = false)
    private String reference;

    @NotNull
    @Column(name = "observation", nullable = false)
    private String observation;

    @NotNull
    @Lob
    @Column(name = "document", nullable = false)
    private byte[] document;

    @Column(name = "document_content_type", nullable = false)
    private String documentContentType;

    @ManyToOne(optional = false)
    @NotNull
    private Operation operation;

    @ManyToOne(optional = false)
    @NotNull
    private Fournisseur fournisseur;

    @ManyToOne(optional = false)
    @NotNull
    private Compte compte;

    @ManyToOne(optional = false)
    @NotNull
    private Mode_de_paiment mode_de_paiment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate_depense() {
        return date_depense;
    }

    public Depense date_depense(LocalDate date_depense) {
        this.date_depense = date_depense;
        return this;
    }

    public void setDate_depense(LocalDate date_depense) {
        this.date_depense = date_depense;
    }

    public LocalDate getDate_echeance() {
        return date_echeance;
    }

    public Depense date_echeance(LocalDate date_echeance) {
        this.date_echeance = date_echeance;
        return this;
    }

    public void setDate_echeance(LocalDate date_echeance) {
        this.date_echeance = date_echeance;
    }

    public String getReference() {
        return reference;
    }

    public Depense reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getObservation() {
        return observation;
    }

    public Depense observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public byte[] getDocument() {
        return document;
    }

    public Depense document(byte[] document) {
        this.document = document;
        return this;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public Depense documentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
        return this;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public Operation getOperation() {
        return operation;
    }

    public Depense operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public Depense fournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        return this;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Compte getCompte() {
        return compte;
    }

    public Depense compte(Compte compte) {
        this.compte = compte;
        return this;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Mode_de_paiment getMode_de_paiment() {
        return mode_de_paiment;
    }

    public Depense mode_de_paiment(Mode_de_paiment mode_de_paiment) {
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
        Depense depense = (Depense) o;
        if (depense.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, depense.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Depense{" +
            "id=" + id +
            ", date_depense='" + date_depense + "'" +
            ", date_echeance='" + date_echeance + "'" +
            ", reference='" + reference + "'" +
            ", observation='" + observation + "'" +
            ", document='" + document + "'" +
            ", documentContentType='" + documentContentType + "'" +
            '}';
    }
}
