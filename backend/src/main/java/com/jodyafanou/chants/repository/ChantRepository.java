package com.jodyafanou.chants.repository;

import com.jodyafanou.chants.domain.Chant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface ChantRepository extends JpaRepository<Chant, Long>, JpaSpecificationExecutor<Chant> {

    /** Charge la catégorie en une seule requête (évite le problème N+1 sur les listes). */
    @Override
    @EntityGraph(attributePaths = "categorie")
    Page<Chant> findAll(Specification<Chant> spec, Pageable pageable);

    @EntityGraph(attributePaths = "categorie")
    Optional<Chant> findWithCategorieById(Long id);

    boolean existsByNumero(Integer numero);

    boolean existsByNumeroAndIdNot(Integer numero, Long id);
}
