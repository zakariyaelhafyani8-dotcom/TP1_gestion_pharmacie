package com.spring_tps.gestion_pharmacie.repository;

import com.spring_tps.gestion_pharmacie.entity.DetailProduit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailProduitRepository extends JpaRepository<DetailProduit, Long> {

    Optional<DetailProduit> findByFabricant(String fabricant);
}
