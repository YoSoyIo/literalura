package com.alurachallenge.literalura.repository;

import com.alurachallenge.literalura.model.Autor;
import com.alurachallenge.literalura.model.DatosAutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre AND a.fechaDeNacimiento = :fechaDeNacimiento AND a.fechaDeFallecimiento = :fechaDeFallecimiento")
    Optional<Autor> findByNameAndBirthYearAndDeathYear(@Param("nombre") String nombre, @Param("fechaDeNacimiento") Integer fechaDeNacimiento, @Param("fechaDeFallecimiento") Integer fechaDeFallecimiento);
}
