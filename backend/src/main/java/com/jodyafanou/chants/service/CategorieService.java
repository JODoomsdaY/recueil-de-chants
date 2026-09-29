package com.jodyafanou.chants.service;

import com.jodyafanou.chants.domain.Categorie;
import com.jodyafanou.chants.dto.CategorieDto;
import com.jodyafanou.chants.repository.CategorieRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategorieService {

    private final CategorieRepository categories;

    public CategorieService(CategorieRepository categories) {
        this.categories = categories;
    }

    public List<CategorieDto> lister() {
        return categories.findAllByOrderByNomAsc().stream().map(CategorieDto::of).toList();
    }

    @Transactional
    public CategorieDto creer(String nom) {
        String propre = nom.trim();
        if (categories.existsByNomIgnoreCase(propre)) {
            throw new ConflitException("La catégorie « " + propre + " » existe déjà.");
        }
        return CategorieDto.of(categories.save(new Categorie(propre)));
    }
}
