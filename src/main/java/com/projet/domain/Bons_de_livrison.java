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
 * A Bons_de_livrison.
 */
@Entity
@Table(name = "bons_de_livrison")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bons_de_livrison")
public class Bons_de_livrison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_livraison", nullable = false)
    private LocalDate date_livraison;

    @Column(name = "objet")
    private String objet;

    @ManyToOne(optional = false)
    @NotNull
    private Condition_de_reglement condition_de_reglement;

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

    public LocalDate getDate_livraison() {
        return date_livraison;
    }

    public Bons_de_livrison date_livraison(LocalDate date_livraison) {
        this.date_livraison = date_livraison;
        return this;
    }

    public void setDate_livraison(LocalDate date_livraison) {
        this.date_livraison = date_livraison;
    }

    public String getObjet() {
        return objet;
    }

    public Bons_de_livrison objet(String objet) {
        this.objet = objet;
        return this;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public Condition_de_reglement getCondition_de_reglement() {
        return condition_de_reglement;
    }

    public Bons_de_livrison condition_de_reglement(Condition_de_reglement condition_de_reglement) {
        this.condition_de_reglement = condition_de_reglement;
        return this;
    }

    public void setCondition_de_reglement(Condition_de_reglement condition_de_reglement) {
        this.condition_de_reglement = condition_de_reglement;
    }

    public Client getClient() {
        return client;
    }

    public Bons_de_livrison client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Mode_de_paiment getMode_de_paiment() {
        return mode_de_paiment;
    }

    public Bons_de_livrison mode_de_paiment(Mode_de_paiment mode_de_paiment) {
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
        Bons_de_livrison bons_de_livrison = (Bons_de_livrison) o;
        if (bons_de_livrison.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bons_de_livrison.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bons_de_livrison{" +
            "id=" + id +
            ", date_livraison='" + date_livraison + "'" +
            ", objet='" + objet + "'" +
            '}';
    }
}
