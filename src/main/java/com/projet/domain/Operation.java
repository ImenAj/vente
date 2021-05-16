package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Operation.
 */
@Entity
@Table(name = "operation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "operation")
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @ManyToOne(optional = false)
    @NotNull
    private SousFamilleDepense sousFamilleDepense;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public Operation designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public SousFamilleDepense getSousFamilleDepense() {
        return sousFamilleDepense;
    }

    public Operation sousFamilleDepense(SousFamilleDepense sousFamilleDepense) {
        this.sousFamilleDepense = sousFamilleDepense;
        return this;
    }

    public void setSousFamilleDepense(SousFamilleDepense sousFamilleDepense) {
        this.sousFamilleDepense = sousFamilleDepense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Operation operation = (Operation) o;
        if (operation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, operation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Operation{" +
            "id=" + id +
            ", designation='" + designation + "'" +
            '}';
    }
}
