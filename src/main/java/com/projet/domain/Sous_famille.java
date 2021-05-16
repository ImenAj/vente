package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Sous_famille.
 */
@Entity
@Table(name = "sous_famille")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sous_famille")
public class Sous_famille implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle_sf", nullable = false)
    private String libelleSF;

    @ManyToOne
    private Famille famille;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleSF() {
        return libelleSF;
    }

    public Sous_famille libelleSF(String libelleSF) {
        this.libelleSF = libelleSF;
        return this;
    }

    public void setLibelleSF(String libelleSF) {
        this.libelleSF = libelleSF;
    }

    public Famille getFamille() {
        return famille;
    }

    public Sous_famille famille(Famille famille) {
        this.famille = famille;
        return this;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sous_famille sous_famille = (Sous_famille) o;
        if (sous_famille.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sous_famille.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sous_famille{" +
            "id=" + id +
            ", libelleSF='" + libelleSF + "'" +
            '}';
    }
}
