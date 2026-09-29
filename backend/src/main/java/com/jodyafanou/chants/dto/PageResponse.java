package com.jodyafanou.chants.dto;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

/** Format de pagination stable, indépendant de l'implémentation Spring Data. */
public record PageResponse<T>(List<T> contenu, int page, int taille, long totalElements, int totalPages) {

    public static <E, T> PageResponse<T> of(Page<E> page, Function<E, T> mapper) {
        return new PageResponse<>(page.getContent().stream().map(mapper).toList(), page.getNumber(),
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
