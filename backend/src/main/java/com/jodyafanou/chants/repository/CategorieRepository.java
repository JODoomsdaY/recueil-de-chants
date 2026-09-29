package com.jodyafanou.chants.repository;

import com.jodyafanou.chants.domain.Categorie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    List<Categorie> findAllByOrderByNomAsc();

    boolean existsByNomIgnoreCase(String nom);
}
