package com.spring_tps.gestion_pharmacie.controller;

import com.spring_tps.gestion_pharmacie.entity.Produit;
import com.spring_tps.gestion_pharmacie.repository.ProduitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ProduitController {

    private final ProduitRepository produitRepository;

    public ProduitController(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @GetMapping("/")
    public String accueil() {
        return "redirect:/produits";
    }

    @GetMapping("/produits")
    public String listerProduits(Model model) {
        model.addAttribute("produits", produitRepository.findAll());
        return "produits/list";
    }

    @GetMapping("/produits/{id}")
    public String afficherProduit(@PathVariable Long id, Model model) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produit introuvable avec l'id " + id));
        model.addAttribute("produit", produit);
        return "produits/view";
    }
}
