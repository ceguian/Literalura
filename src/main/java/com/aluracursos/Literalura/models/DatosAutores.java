package com.aluracursos.Literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutores(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Number añoNacimiento,
        @JsonAlias("death_year") Number añoDeceso
) {
}
