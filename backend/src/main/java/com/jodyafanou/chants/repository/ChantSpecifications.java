package com.jodyafanou.chants.repository;

import com.jodyafanou.chants.domain.Chant;
import org.springframework.data.jpa.domain.Specification;

/** Filtres combinables pour la recherche de chants. */
public final class ChantSpecifications {

    private ChantSpecifications() {
    }

    /** Recherche insensible à la casse dans le titre, l'auteur et les paroles. */
    public static Specification<Chant> texte(String q) {
        if (q == null || q.isBlank()) {
            return null;
        }
        String motif = "%" + q.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("titre")), motif),
                cb.like(cb.lower(root.get("auteur")), motif),
                cb.like(cb.lower(root.get("paroles")), motif));
    }

    public static Specification<Chant> categorie(Long categorieId) {
        return categorieId == null ? null
                : (root, query, cb) -> cb.equal(root.get("categorie").get("id"), categorieId);
    }

    public static Specification<Chant> langue(String langue) {
        return langue == null || langue.isBlank() ? null
                : (root, query, cb) -> cb.equal(root.get("langue"), langue);
    }
}
