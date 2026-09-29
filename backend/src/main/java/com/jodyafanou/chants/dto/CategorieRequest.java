package com.jodyafanou.chants.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategorieRequest(@NotBlank @Size(max = 80) String nom) {
}
