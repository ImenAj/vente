package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Famille.
 */
@Entity
@Table(name = "famille")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "famille")
public class Famille implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle_f", nullable = false)
    private String libelleF;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleF() {
        return libelleF;
    }

    public Famille libelleF(String libelleF) {
        this.libelleF = libelleF;
        return this;
    }

    public void setLibelleF(String libelleF) {
        this.libelleF = libelleF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Famille famille = (Famille) o;
        if (famille.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, famille.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Famille{" +
            "id=" + id +
            ", libelleF='" + libelleF + "'" +
            '}';
    }
}
