package com.spring_tps.gestion_pharmacie.controller;

import com.spring_tps.gestion_pharmacie.entity.Categorie;
import com.spring_tps.gestion_pharmacie.entity.DetailProduit;
import com.spring_tps.gestion_pharmacie.entity.Fournisseur;
import com.spring_tps.gestion_pharmacie.entity.Produit;
import com.spring_tps.gestion_pharmacie.entity.Tag;
import com.spring_tps.gestion_pharmacie.repository.CategorieRepository;
import com.spring_tps.gestion_pharmacie.repository.FournisseurRepository;
import com.spring_tps.gestion_pharmacie.repository.ProduitRepository;
import com.spring_tps.gestion_pharmacie.repository.TagRepository;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitRestController {

    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;
    private final FournisseurRepository fournisseurRepository;
    private final TagRepository tagRepository;

    public ProduitRestController(ProduitRepository produitRepository,
                                 CategorieRepository categorieRepository,
                                 FournisseurRepository fournisseurRepository,
                                 TagRepository tagRepository) {
        this.produitRepository = produitRepository;
        this.categorieRepository = categorieRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        return produitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produit introuvable avec l'id " + id));
    }

    @PostMapping
    public ResponseEntity<Produit> createProduit(@RequestBody Produit produit) {
        produit.setId(null);
        appliquerRelations(produit);
        Produit saved = produitRepository.save(produit);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id,
                                                 @RequestBody Produit payload) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produit introuvable avec l'id " + id));

        produit.setNom(payload.getNom());
        produit.setDescription(payload.getDescription());
        produit.setPrix(payload.getPrix());
        produit.setCodeBarre(payload.getCodeBarre());
        produit.setDateExpiration(payload.getDateExpiration());

        if (payload.getCategorie() != null) {
            produit.setCategorie(resoudreCategorie(payload.getCategorie()));
        }
        if (payload.getFournisseur() != null) {
            produit.setFournisseur(resoudreFournisseur(payload.getFournisseur()));
        }
        if (payload.getDetailProduit() != null) {
            if (produit.getDetailProduit() != null) {
                copierDetail(produit.getDetailProduit(), payload.getDetailProduit());
            } else {
                DetailProduit detail = payload.getDetailProduit();
                detail.setId(null);
                produit.setDetailProduit(detail);
            }
        }
        produit.setTags(resoudreTags(payload.getTags()));

        return ResponseEntity.ok(produitRepository.save(produit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        if (!produitRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Produit introuvable avec l'id " + id);
        }
        produitRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void appliquerRelations(Produit produit) {
        produit.setCategorie(resoudreCategorie(produit.getCategorie()));
        produit.setFournisseur(resoudreFournisseur(produit.getFournisseur()));
        produit.setTags(resoudreTags(produit.getTags()));
    }

    private Categorie resoudreCategorie(Categorie categorie) {
        if (categorie == null) {
            return null;
        }
        if (categorie.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "L'identifiant de la catégorie est obligatoire");
        }
        return categorieRepository.findById(categorie.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Catégorie introuvable avec l'id " + categorie.getId()));
    }

    private Fournisseur resoudreFournisseur(Fournisseur fournisseur) {
        if (fournisseur == null) {
            return null;
        }
        if (fournisseur.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "L'identifiant du fournisseur est obligatoire");
        }
        return fournisseurRepository.findById(fournisseur.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Fournisseur introuvable avec l'id " + fournisseur.getId()));
    }

    private List<Tag> resoudreTags(List<Tag> tags) {
        List<Tag> resultat = new ArrayList<>();
        if (tags == null) {
            return resultat;
        }
        for (Tag tag : tags) {
            if (tag == null || tag.getId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "L'identifiant de chaque tag est obligatoire");
            }
            Tag tagExistant = tagRepository.findById(tag.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Tag introuvable avec l'id " + tag.getId()));
            resultat.add(tagExistant);
        }
        return resultat;
    }

    private void copierDetail(DetailProduit cible, DetailProduit source) {
        cible.setFabricant(source.getFabricant());
        cible.setPaysOrigine(source.getPaysOrigine());
        cible.setComposition(source.getComposition());
        cible.setDosage(source.getDosage());
        cible.setIndications(source.getIndications());
    }
}
