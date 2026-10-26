package com.spring_tps.gestion_pharmacie.repository;

import com.spring_tps.gestion_pharmacie.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    Optional<Fournisseur> findByNom(String nom);
}
