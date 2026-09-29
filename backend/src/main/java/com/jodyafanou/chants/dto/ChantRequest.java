package com.jodyafanou.chants.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ChantRequest(
        @Positive Integer numero,
        @NotBlank @Size(max = 200) String titre,
        @Size(max = 150) String auteur,
        @NotBlank @Size(max = 10) String langue,
        @Size(max = 20) String tonalite,
        @NotBlank @Size(max = 10_000) String paroles,
        Long categorieId) {
}
