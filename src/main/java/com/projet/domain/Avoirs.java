package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Avoirs.
 */
@Entity
@Table(name = "avoirs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "avoirs")
public class Avoirs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "numero_facture_avoirs", nullable = false)
    private Integer numero_factureAvoirs;

    @ManyToOne(optional = false)
    @NotNull
    private Facture facture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero_factureAvoirs() {
        return numero_factureAvoirs;
    }

    public Avoirs numero_factureAvoirs(Integer numero_factureAvoirs) {
        this.numero_factureAvoirs = numero_factureAvoirs;
        return this;
    }

    public void setNumero_factureAvoirs(Integer numero_factureAvoirs) {
        this.numero_factureAvoirs = numero_factureAvoirs;
    }

    public Facture getFacture() {
        return facture;
    }

    public Avoirs facture(Facture facture) {
        this.facture = facture;
        return this;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Avoirs avoirs = (Avoirs) o;
        if (avoirs.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, avoirs.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Avoirs{" +
            "id=" + id +
            ", numero_factureAvoirs='" + numero_factureAvoirs + "'" +
            '}';
    }
}
