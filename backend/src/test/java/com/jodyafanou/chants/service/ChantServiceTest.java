package com.jodyafanou.chants.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jodyafanou.chants.domain.Categorie;
import com.jodyafanou.chants.dto.ChantRequest;
import com.jodyafanou.chants.dto.ChantResponse;
import com.jodyafanou.chants.dto.ChantResume;
import com.jodyafanou.chants.dto.PageResponse;
import com.jodyafanou.chants.repository.CategorieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(ChantService.class)
class ChantServiceTest {

    @Autowired
    ChantService service;

    @Autowired
    CategorieRepository categories;

    Categorie louange;
    Categorie noel;

    @BeforeEach
    void donnees() {
        louange = categories.save(new Categorie("Louange"));
        noel = categories.save(new Categorie("Noël"));
        service.creer(requete(1, "Chant du matin", "fr", "Le soleil se lève", louange.getId()));
        service.creer(requete(2, "Nuit étoilée", "fr", "Une étoile brille dans la nuit", noel.getId()));
        service.creer(requete(null, "Morning song", "en", "The sun is rising", louange.getId()));
    }

    @Test
    void recherche_dans_les_paroles_sans_tenir_compte_de_la_casse() {
        PageResponse<ChantResume> page = service.rechercher("ÉTOILE", null, null, 0, 20);

        assertThat(page.contenu()).extracting(ChantResume::titre).containsExactly("Nuit étoilée");
    }

    @Test
    void filtre_par_categorie_et_langue() {
        assertThat(service.rechercher(null, louange.getId(), null, 0, 20).totalElements()).isEqualTo(2);
        assertThat(service.rechercher(null, louange.getId(), "en", 0, 20).contenu())
                .extracting(ChantResume::titre)
                .containsExactly("Morning song");
    }

    @Test
    void pagine_les_resultats() {
        PageResponse<ChantResume> page = service.rechercher(null, null, null, 1, 2);

        assertThat(page.totalElements()).isEqualTo(3);
        assertThat(page.totalPages()).isEqualTo(2);
        assertThat(page.contenu()).hasSize(1);
    }

    @Test
    void refuse_un_numero_deja_utilise() {
        assertThatThrownBy(() -> service.creer(requete(1, "Doublon", "fr", "…", null)))
                .isInstanceOf(ConflitException.class)
                .hasMessageContaining("1");
    }

    @Test
    void modifie_un_chant_en_conservant_son_numero() {
        Long id = service.rechercher("matin", null, null, 0, 20).contenu().getFirst().id();

        ChantResponse modifie = service.modifier(id,
                requete(1, "Chant du matin (v2)", "fr", "  Le soleil se lève encore  ", noel.getId()));

        assertThat(modifie.titre()).isEqualTo("Chant du matin (v2)");
        assertThat(modifie.paroles()).isEqualTo("Le soleil se lève encore");
        assertThat(modifie.categorie().nom()).isEqualTo("Noël");
    }

    @Test
    void chant_ou_categorie_inexistants() {
        assertThatThrownBy(() -> service.obtenir(999L)).isInstanceOf(RessourceIntrouvableException.class);
        assertThatThrownBy(() -> service.creer(requete(null, "Sans catégorie", "fr", "…", 999L)))
                .isInstanceOf(RessourceIntrouvableException.class);
    }

    private static ChantRequest requete(Integer numero, String titre, String langue, String paroles,
                                        Long categorieId) {
        return new ChantRequest(numero, titre, null, langue, null, paroles, categorieId);
    }
}
