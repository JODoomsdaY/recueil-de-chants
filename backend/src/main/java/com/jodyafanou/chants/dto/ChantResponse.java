package com.jodyafanou.chants.dto;

import com.jodyafanou.chants.domain.Chant;
import java.time.Instant;

public record ChantResponse(
        Long id,
        Integer numero,
        String titre,
        String auteur,
        String langue,
        String tonalite,
        String paroles,
        CategorieDto categorie,
        Instant creeLe,
        Instant modifieLe) {

    public static ChantResponse of(Chant c) {
        return new ChantResponse(c.getId(), c.getNumero(), c.getTitre(), c.getAuteur(), c.getLangue(),
                c.getTonalite(), c.getParoles(), CategorieDto.of(c.getCategorie()), c.getCreeLe(),
                c.getModifieLe());
    }
}
