package com.joaobrandao.Literalura.repository;

import com.joaobrandao.Literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    boolean existsByNome(String nome);
}
