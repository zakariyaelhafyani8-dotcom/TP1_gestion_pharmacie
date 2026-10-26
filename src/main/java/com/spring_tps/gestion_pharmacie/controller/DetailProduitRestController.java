package com.spring_tps.gestion_pharmacie.controller;

import com.spring_tps.gestion_pharmacie.entity.DetailProduit;
import com.spring_tps.gestion_pharmacie.repository.DetailProduitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/details-produits")
public class DetailProduitRestController {

    private final DetailProduitRepository detailProduitRepository;

    public DetailProduitRestController(DetailProduitRepository detailProduitRepository) {
        this.detailProduitRepository = detailProduitRepository;
    }

    @GetMapping
    public List<DetailProduit> getAllDetails() {
        return detailProduitRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailProduit> getDetailById(@PathVariable Long id) {
        return detailProduitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Détail produit introuvable avec l'id " + id));
    }

    @PostMapping
    public ResponseEntity<DetailProduit> createDetail(@RequestBody DetailProduit detailProduit) {
        detailProduit.setId(null);
        DetailProduit saved = detailProduitRepository.save(detailProduit);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetailProduit> updateDetail(@PathVariable Long id,
                                                      @RequestBody DetailProduit payload) {
        DetailProduit detail = detailProduitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Détail produit introuvable avec l'id " + id));
        detail.setFabricant(payload.getFabricant());
        detail.setPaysOrigine(payload.getPaysOrigine());
        detail.setComposition(payload.getComposition());
        detail.setDosage(payload.getDosage());
        detail.setIndications(payload.getIndications());
        return ResponseEntity.ok(detailProduitRepository.save(detail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetail(@PathVariable Long id) {
        if (!detailProduitRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Détail produit introuvable avec l'id " + id);
        }
        detailProduitRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
