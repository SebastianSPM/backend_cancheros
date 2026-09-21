package com.generation.grupo10.repository;
import com.generation.grupo10.model.Contactos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ContactosRepository extends JpaRepository<Contactos, Integer> {
}