package com.spring_tps.gestion_pharmacie.repository;

import com.spring_tps.gestion_pharmacie.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    Optional<Categorie> findByNom(String nom);
}
