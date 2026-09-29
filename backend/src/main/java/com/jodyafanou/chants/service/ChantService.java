package com.jodyafanou.chants.service;

import com.jodyafanou.chants.domain.Categorie;
import com.jodyafanou.chants.domain.Chant;
import com.jodyafanou.chants.dto.ChantRequest;
import com.jodyafanou.chants.dto.ChantResponse;
import com.jodyafanou.chants.dto.ChantResume;
import com.jodyafanou.chants.dto.PageResponse;
import com.jodyafanou.chants.repository.CategorieRepository;
import com.jodyafanou.chants.repository.ChantRepository;
import com.jodyafanou.chants.repository.ChantSpecifications;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChantService {

    private static final int TAILLE_MAX = 100;

    private final ChantRepository chants;
    private final CategorieRepository categories;

    public ChantService(ChantRepository chants, CategorieRepository categories) {
        this.chants = chants;
        this.categories = categories;
    }

    public PageResponse<ChantResume> rechercher(String q, Long categorieId, String langue, int page, int taille) {
        List<Specification<Chant>> filtres = Stream.of(
                        ChantSpecifications.texte(q),
                        ChantSpecifications.categorie(categorieId),
                        ChantSpecifications.langue(langue))
                .filter(Objects::nonNull)
                .toList();

        // Tri par numéro du recueil, puis par titre
        Sort tri = Sort.by("numero", "titre");
        PageRequest pageRequest = PageRequest.of(Math.max(page, 0), Math.clamp(taille, 1, TAILLE_MAX), tri);

        return PageResponse.of(chants.findAll(Specification.allOf(filtres), pageRequest), ChantResume::of);
    }

    public ChantResponse obtenir(Long id) {
        return ChantResponse.of(trouver(id));
    }

    @Transactional
    public ChantResponse creer(ChantRequest requete) {
        if (requete.numero() != null && chants.existsByNumero(requete.numero())) {
            throw new ConflitException("Le numéro " + requete.numero() + " est déjà utilisé.");
        }
        Chant chant = new Chant();
        appliquer(chant, requete);
        return ChantResponse.of(chants.save(chant));
    }

    @Transactional
    public ChantResponse modifier(Long id, ChantRequest requete) {
        Chant chant = trouver(id);
        if (requete.numero() != null && chants.existsByNumeroAndIdNot(requete.numero(), id)) {
            throw new ConflitException("Le numéro " + requete.numero() + " est déjà utilisé.");
        }
        appliquer(chant, requete);
        return ChantResponse.of(chants.saveAndFlush(chant));
    }

    @Transactional
    public void supprimer(Long id) {
        chants.delete(trouver(id));
    }

    private Chant trouver(Long id) {
        return chants.findWithCategorieById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Chant " + id + " introuvable."));
    }

    private void appliquer(Chant chant, ChantRequest r) {
        chant.setNumero(r.numero());
        chant.setTitre(r.titre().trim());
        chant.setAuteur(blancVersNull(r.auteur()));
        chant.setLangue(r.langue());
        chant.setTonalite(blancVersNull(r.tonalite()));
        chant.setParoles(r.paroles().strip());
        chant.setCategorie(r.categorieId() == null ? null : categorie(r.categorieId()));
    }

    private Categorie categorie(Long id) {
        return categories.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Catégorie " + id + " introuvable."));
    }

    private static String blancVersNull(String valeur) {
        return valeur == null || valeur.isBlank() ? null : valeur.trim();
    }
}
