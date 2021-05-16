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
 * A Devis.
 */
@Entity
@Table(name = "devis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "devis")
public class Devis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "numero_devis", nullable = false)
    private Integer numero_Devis;

    @NotNull
    @Column(name = "date_devis", nullable = false)
    private LocalDate date_Devis;

    @NotNull
    @Column(name = "montant", nullable = false)
    private Double montant;

    @ManyToOne(optional = false)
    @NotNull
    private Client client;

    @ManyToOne(optional = false)
    @NotNull
    private Mode_de_paiment mode_de_paiment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero_Devis() {
        return numero_Devis;
    }

    public Devis numero_Devis(Integer numero_Devis) {
        this.numero_Devis = numero_Devis;
        return this;
    }

    public void setNumero_Devis(Integer numero_Devis) {
        this.numero_Devis = numero_Devis;
    }

    public LocalDate getDate_Devis() {
        return date_Devis;
    }

    public Devis date_Devis(LocalDate date_Devis) {
        this.date_Devis = date_Devis;
        return this;
    }

    public void setDate_Devis(LocalDate date_Devis) {
        this.date_Devis = date_Devis;
    }

    public Double getMontant() {
        return montant;
    }

    public Devis montant(Double montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Client getClient() {
        return client;
    }

    public Devis client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Mode_de_paiment getMode_de_paiment() {
        return mode_de_paiment;
    }

    public Devis mode_de_paiment(Mode_de_paiment mode_de_paiment) {
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
        Devis devis = (Devis) o;
        if (devis.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, devis.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Devis{" +
            "id=" + id +
            ", numero_Devis='" + numero_Devis + "'" +
            ", date_Devis='" + date_Devis + "'" +
            ", montant='" + montant + "'" +
            '}';
    }
}
