-- ============================================================
-- TP 1 : Gestion de stock de pharmacie
-- Base de donnees MySQL + jeu de donnees
-- ============================================================

CREATE DATABASE IF NOT EXISTS gestion_pharmacie
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gestion_pharmacie;

-- ============================================================
-- Tables
-- ============================================================

CREATE TABLE IF NOT EXISTS categorie (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom         VARCHAR(255) NOT NULL,
    description VARCHAR(1000)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS fournisseur (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom     VARCHAR(255) NOT NULL,
    contact VARCHAR(255),
    adresse VARCHAR(255)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `detailProduit` (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    fabricant    VARCHAR(255),
    paysOrigine  VARCHAR(255),
    composition  VARCHAR(1000),
    dosage       VARCHAR(255),
    indications  VARCHAR(1000)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS tag (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(255) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS produit (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom             VARCHAR(255) NOT NULL,
    description     VARCHAR(1000),
    prix            DECIMAL(10, 2),
    code_barre      VARCHAR(255) UNIQUE,
    date_expiration DATE,
    categorie_id    BIGINT,
    fournisseur_id  BIGINT,
    detail_id       BIGINT UNIQUE,
    CONSTRAINT fk_produit_categorie
        FOREIGN KEY (categorie_id) REFERENCES categorie (id),
    CONSTRAINT fk_produit_fournisseur
        FOREIGN KEY (fournisseur_id) REFERENCES fournisseur (id),
    CONSTRAINT fk_produit_detail
        FOREIGN KEY (detail_id) REFERENCES `detailProduit` (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS produit_tag (
    produit_id BIGINT NOT NULL,
    tag_id     BIGINT NOT NULL,
    PRIMARY KEY (produit_id, tag_id),
    CONSTRAINT fk_produit_tag_produit
        FOREIGN KEY (produit_id) REFERENCES produit (id) ON DELETE CASCADE,
    CONSTRAINT fk_produit_tag_tag
        FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- ============================================================
-- Jeu de donnees
-- ============================================================

INSERT IGNORE INTO categorie (id, nom, description) VALUES
(1, 'Antibiotiques', 'Médicaments utilisés pour traiter les infections bactériennes.'),
(2, 'Antalgiques', 'Médicaments pour soulager la douleur.'),
(3, 'Vitamines', 'Compléments nutritionnels et fortifiants.'),
(4, 'Antiseptiques', 'Produits désinfectants pour usage externe.');

INSERT IGNORE INTO fournisseur (id, nom, contact, adresse) VALUES
(1, 'PharmaMaroc', 'contact@pharmamaroc.ma', 'Casablanca, Maroc'),
(2, 'Medisup France', 'info@medisup.fr', 'Paris, France'),
(3, 'Sandoz', 'export@sandoz.com', 'Bâle, Suisse'),
(4, 'Cooper Pharma', 'support@cooperpharma.ma', 'Rabat, Maroc');

INSERT IGNORE INTO `detailProduit` (id, fabricant, paysOrigine, composition, dosage, indications) VALUES
(1, 'Sanofi', 'France', 'Amoxicilline trihydratée 500 mg', '500 mg', 'Infections ORL et respiratoires'),
(2, 'Bayer', 'Allemagne', 'Paracétamol 500 mg', '500 mg', 'Douleurs légères à modérées, fièvre'),
(3, 'Pfizer', 'États-Unis', 'Vitamine C 1000 mg', '1000 mg', 'Renforcement immunitaire'),
(4, 'Johnson & Johnson', 'Maroc', 'Povidone iodée 10%', '10%', 'Désinfection des plaies'),
(5, 'GSK', 'Royaume-Uni', 'Ibuprofène 400 mg', '400 mg', 'Inflammations et douleurs musculaires');

INSERT IGNORE INTO tag (id, libelle) VALUES
(1, 'Sans ordonnance'),
(2, 'Prescription obligatoire'),
(3, 'En promotion'),
(4, 'Stock limité'),
(5, 'Importé');

INSERT IGNORE INTO produit (id, nom, description, prix, code_barre, date_expiration,
                             categorie_id, fournisseur_id, detail_id) VALUES
(1, 'Amoxicilline 500mg', 'Antibiotique à large spectre.', 45.00, 'AMX500-2025',
 '2026-03-10', 1, 1, 1),
(2, 'Doliprane 500mg', 'Antalgique et antipyrétique.', 25.50, 'DLP500-2025',
 '2025-11-01', 2, 2, 2),
(3, 'Vitamine C 1000mg', 'Complément pour renforcer les défenses.', 30.00,
 'VITC1000-2025', '2026-06-15', 3, 3, 3),
(4, 'Bétadine Solution 10%', 'Désinfectant pour usage externe.', 18.00,
 'BETADINE10-2025', '2027-01-30', 4, 4, 4),
(5, 'Nurofen 400mg', 'Soulage la douleur et la fièvre.', 42.00, 'NURO400-2025',
 '2026-02-20', 2, 3, 5);

INSERT IGNORE INTO produit_tag (produit_id, tag_id) VALUES
(1, 2),
(2, 1),
(2, 3),
(3, 1),
(3, 5),
(4, 1),
(4, 4),
(5, 1),
(5, 5);
