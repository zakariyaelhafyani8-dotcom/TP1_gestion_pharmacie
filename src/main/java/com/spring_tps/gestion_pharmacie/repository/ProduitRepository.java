package com.spring_tps.gestion_pharmacie.repository;

import com.spring_tps.gestion_pharmacie.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    List<Produit> findByNomContainingIgnoreCase(String nom);

    List<Produit> findByCategorieId(Long categorieId);

    List<Produit> findByFournisseurId(Long fournisseurId);
}
