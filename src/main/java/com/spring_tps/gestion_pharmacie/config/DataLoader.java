package com.spring_tps.gestion_pharmacie.config;

import com.spring_tps.gestion_pharmacie.entity.Categorie;
import com.spring_tps.gestion_pharmacie.entity.DetailProduit;
import com.spring_tps.gestion_pharmacie.entity.Fournisseur;
import com.spring_tps.gestion_pharmacie.entity.Produit;
import com.spring_tps.gestion_pharmacie.entity.Tag;
import com.spring_tps.gestion_pharmacie.repository.CategorieRepository;
import com.spring_tps.gestion_pharmacie.repository.DetailProduitRepository;
import com.spring_tps.gestion_pharmacie.repository.FournisseurRepository;
import com.spring_tps.gestion_pharmacie.repository.ProduitRepository;
import com.spring_tps.gestion_pharmacie.repository.TagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategorieRepository categorieRepository;
    private final FournisseurRepository fournisseurRepository;
    private final DetailProduitRepository detailProduitRepository;
    private final TagRepository tagRepository;
    private final ProduitRepository produitRepository;

    public DataLoader(CategorieRepository categorieRepository,
                      FournisseurRepository fournisseurRepository,
                      DetailProduitRepository detailProduitRepository,
                      TagRepository tagRepository,
                      ProduitRepository produitRepository) {
        this.categorieRepository = categorieRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.detailProduitRepository = detailProduitRepository;
        this.tagRepository = tagRepository;
        this.produitRepository = produitRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (produitRepository.count() > 0) {
            return;
        }

        Categorie antibiotiques = getOrCreateCategorie("Antibiotiques",
                "Médicaments utilisés pour traiter les infections bactériennes.");
        Categorie antalgiques = getOrCreateCategorie("Antalgiques",
                "Médicaments pour soulager la douleur.");
        Categorie vitamines = getOrCreateCategorie("Vitamines",
                "Compléments nutritionnels et fortifiants.");
        Categorie antiseptiques = getOrCreateCategorie("Antiseptiques",
                "Produits désinfectants pour usage externe.");

        Fournisseur pharmaMaroc = getOrCreateFournisseur("PharmaMaroc",
                "contact@pharmamaroc.ma", "Casablanca, Maroc");
        Fournisseur medisup = getOrCreateFournisseur("Medisup France",
                "info@medisup.fr", "Paris, France");
        Fournisseur sandoz = getOrCreateFournisseur("Sandoz",
                "export@sandoz.com", "Bâle, Suisse");
        Fournisseur cooper = getOrCreateFournisseur("Cooper Pharma",
                "support@cooperpharma.ma", "Rabat, Maroc");

        DetailProduit detailAmoxicilline = getOrCreateDetail("Sanofi", "France",
                "Amoxicilline trihydratée 500 mg", "500 mg",
                "Infections ORL et respiratoires");
        DetailProduit detailDoliprane = getOrCreateDetail("Bayer", "Allemagne",
                "Paracétamol 500 mg", "500 mg",
                "Douleurs légères à modérées, fièvre");
        DetailProduit detailVitamineC = getOrCreateDetail("Pfizer", "États-Unis",
                "Vitamine C 1000 mg", "1000 mg",
                "Renforcement immunitaire");
        DetailProduit detailBetadine = getOrCreateDetail("Johnson & Johnson", "Maroc",
                "Povidone iodée 10%", "10%",
                "Désinfection des plaies");
        DetailProduit detailNurofen = getOrCreateDetail("GSK", "Royaume-Uni",
                "Ibuprofène 400 mg", "400 mg",
                "Inflammations et douleurs musculaires");

        Tag sansOrdonnance = getOrCreateTag("Sans ordonnance");
        Tag prescription = getOrCreateTag("Prescription obligatoire");
        Tag promotion = getOrCreateTag("En promotion");
        Tag stockLimite = getOrCreateTag("Stock limité");
        Tag importe = getOrCreateTag("Importé");

        produitRepository.save(new Produit(
                null,
                "Amoxicilline 500mg",
                "Antibiotique à large spectre.",
                new BigDecimal("45.00"),
                "AMX500-2025",
                LocalDate.parse("2026-03-10"),
                antibiotiques,
                pharmaMaroc,
                detailAmoxicilline,
                List.of(prescription)
        ));

        produitRepository.save(new Produit(
                null,
                "Doliprane 500mg",
                "Antalgique et antipyrétique.",
                new BigDecimal("25.50"),
                "DLP500-2025",
                LocalDate.parse("2025-11-01"),
                antalgiques,
                medisup,
                detailDoliprane,
                List.of(sansOrdonnance, promotion)
        ));

        produitRepository.save(new Produit(
                null,
                "Vitamine C 1000mg",
                "Complément pour renforcer les défenses.",
                new BigDecimal("30.00"),
                "VITC1000-2025",
                LocalDate.parse("2026-06-15"),
                vitamines,
                sandoz,
                detailVitamineC,
                List.of(sansOrdonnance, importe)
        ));

        produitRepository.save(new Produit(
                null,
                "Bétadine Solution 10%",
                "Désinfectant pour usage externe.",
                new BigDecimal("18.00"),
                "BETADINE10-2025",
                LocalDate.parse("2027-01-30"),
                antiseptiques,
                cooper,
                detailBetadine,
                List.of(sansOrdonnance, stockLimite)
        ));

        produitRepository.save(new Produit(
                null,
                "Nurofen 400mg",
                "Soulage la douleur et la fièvre.",
                new BigDecimal("42.00"),
                "NURO400-2025",
                LocalDate.parse("2026-02-20"),
                antalgiques,
                sandoz,
                detailNurofen,
                List.of(sansOrdonnance, importe)
        ));
    }

    private Categorie getOrCreateCategorie(String nom, String description) {
        return categorieRepository.findByNom(nom).orElseGet(() -> {
            Categorie categorie = new Categorie(null, nom, description);
            return categorieRepository.save(categorie);
        });
    }

    private Fournisseur getOrCreateFournisseur(String nom, String contact, String adresse) {
        return fournisseurRepository.findByNom(nom).orElseGet(() -> {
            Fournisseur fournisseur = new Fournisseur(null, nom, contact, adresse);
            return fournisseurRepository.save(fournisseur);
        });
    }

    private DetailProduit getOrCreateDetail(String fabricant, String paysOrigine,
                                            String composition, String dosage,
                                            String indications) {
        return detailProduitRepository.findByFabricant(fabricant).orElseGet(() -> {
            DetailProduit detail = new DetailProduit(null, fabricant, paysOrigine,
                    composition, dosage, indications);
            return detailProduitRepository.save(detail);
        });
    }

    private Tag getOrCreateTag(String libelle) {
        return tagRepository.findByLibelle(libelle).orElseGet(() -> {
            Tag tag = new Tag(null, libelle);
            return tagRepository.save(tag);
        });
    }
}
