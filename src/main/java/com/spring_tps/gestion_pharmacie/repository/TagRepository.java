package com.spring_tps.gestion_pharmacie.repository;

import com.spring_tps.gestion_pharmacie.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByLibelle(String libelle);
}
