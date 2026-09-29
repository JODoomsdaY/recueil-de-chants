package com.jodyafanou.chants.dto;

import com.jodyafanou.chants.domain.Chant;

/** Version allégée pour les listes : sans les paroles. */
public record ChantResume(Long id, Integer numero, String titre, String auteur, String langue,
                          CategorieDto categorie) {

    public static ChantResume of(Chant c) {
        return new ChantResume(c.getId(), c.getNumero(), c.getTitre(), c.getAuteur(), c.getLangue(),
                CategorieDto.of(c.getCategorie()));
    }
}
