package com.matheuspinheiro.dic_logistica.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Palavra.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
