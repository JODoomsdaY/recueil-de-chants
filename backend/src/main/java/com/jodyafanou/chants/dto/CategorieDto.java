package com.jodyafanou.chants.dto;

import com.jodyafanou.chants.domain.Categorie;

public record CategorieDto(Long id, String nom) {

    public static CategorieDto of(Categorie categorie) {
        return categorie == null ? null : new CategorieDto(categorie.getId(), categorie.getNom());
    }
}
