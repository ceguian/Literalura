package com.aluracursos.Literalura.DTO;
import com.aluracursos.Literalura.models.DatosAutores;


import java.util.List;

public record LibroDTO(
        Long id,
        String titulo,
        List<String> generos,
        List<DatosAutores> autores,
        List<String> lenguajes,
        Number cuentaDescargas

) {
}
