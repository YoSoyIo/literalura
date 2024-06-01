package com.alurachallenge.literalura.repository;

import com.alurachallenge.literalura.model.Autor;
import com.alurachallenge.literalura.model.DatosAutor;
import com.alurachallenge.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String nombreLibro);

}
