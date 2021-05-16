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
 * A CommandeClient.
 */
@Entity
@Table(name = "commande_client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "commandeclient")
public class CommandeClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "numero_commande", nullable = false)
    private Integer numeroCommande;

    @NotNull
    @Column(name = "date_commande", nullable = false)
    private LocalDate dateCommande;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroCommande() {
        return numeroCommande;
    }

    public CommandeClient numeroCommande(Integer numeroCommande) {
        this.numeroCommande = numeroCommande;
        return this;
    }

    public void setNumeroCommande(Integer numeroCommande) {
        this.numeroCommande = numeroCommande;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public CommandeClient dateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
        return this;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandeClient commandeClient = (CommandeClient) o;
        if (commandeClient.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, commandeClient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CommandeClient{" +
            "id=" + id +
            ", numeroCommande='" + numeroCommande + "'" +
            ", dateCommande='" + dateCommande + "'" +
            '}';
    }
}
