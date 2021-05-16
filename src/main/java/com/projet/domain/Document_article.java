package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Document_article.
 */
@Entity
@Table(name = "document_article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "document_article")
public class Document_article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Column(name = "path")
    private byte[] path;

    @Column(name = "path_content_type")
    private String pathContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPath() {
        return path;
    }

    public Document_article path(byte[] path) {
        this.path = path;
        return this;
    }

    public void setPath(byte[] path) {
        this.path = path;
    }

    public String getPathContentType() {
        return pathContentType;
    }

    public Document_article pathContentType(String pathContentType) {
        this.pathContentType = pathContentType;
        return this;
    }

    public void setPathContentType(String pathContentType) {
        this.pathContentType = pathContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Document_article document_article = (Document_article) o;
        if (document_article.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, document_article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Document_article{" +
            "id=" + id +
            ", path='" + path + "'" +
            ", pathContentType='" + pathContentType + "'" +
            '}';
    }
}
