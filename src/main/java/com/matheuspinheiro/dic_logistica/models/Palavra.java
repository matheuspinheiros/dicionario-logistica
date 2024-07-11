package com.matheuspinheiro.dic_logistica.models;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = Palavra.TABLE_NAME)
public class Palavra {

    public interface CreatePalavra {
    }

    public interface UpdatePalavra {
    }

    public static final String TABLE_NAME = "dicionario";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "palavra", length = 100, unique = true, nullable = false)
    @NotEmpty(groups = CreatePalavra.class)
    @NotNull(groups = CreatePalavra.class)
    @Size(min = 1, max = 100)
    private String palavra;

    @Column(name = "significado", nullable = false, columnDefinition = "text")
    @NotEmpty(groups = { CreatePalavra.class, UpdatePalavra.class })
    @NotNull(groups = { CreatePalavra.class, UpdatePalavra.class })
    private String significado;

    public Palavra() {
    }

    public Palavra(Long id, String palavra, String significado) {
        this.id = id;
        this.palavra = palavra;
        this.significado = significado;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPalavra() {
        return this.palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public String getSignificado() {
        return this.significado;
    }

    public void setSignificado(String significado) {
        this.significado = significado;
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;

        Palavra other = (Palavra) obj;
        if (this.id == null)
            if (other.id != null)
                return false;
            else if (!this.id.equals(other.id))
                return false;
        return Objects.equals(this.id, other.id) && Objects.equals(this.palavra, other.palavra)
                && Objects.equals(this.significado, other.significado);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }

}
