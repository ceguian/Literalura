package com.aluracursos.Literalura.repositories;
import com.aluracursos.Literalura.models.Idioma;
import com.aluracursos.Literalura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTituloContainsIgnoreCase(String nombreLibro);

    List<Libro> findBylenguajes(Idioma idioma);




}
