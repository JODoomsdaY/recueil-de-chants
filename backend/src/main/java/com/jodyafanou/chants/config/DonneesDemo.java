package com.jodyafanou.chants.config;

import com.jodyafanou.chants.domain.Categorie;
import com.jodyafanou.chants.domain.Chant;
import com.jodyafanou.chants.repository.CategorieRepository;
import com.jodyafanou.chants.repository.ChantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Jeu de données de démonstration, chargé au premier lancement.
 * Les textes sont des exemples rédigés pour le projet.
 */
@Component
@ConditionalOnProperty(name = "app.seed", havingValue = "true")
public class DonneesDemo implements CommandLineRunner {

    private final ChantRepository chants;
    private final CategorieRepository categories;

    public DonneesDemo(ChantRepository chants, CategorieRepository categories) {
        this.chants = chants;
        this.categories = categories;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (chants.count() > 0) {
            return;
        }
        Categorie louange = categories.save(new Categorie("Louange"));
        Categorie adoration = categories.save(new Categorie("Adoration"));
        Categorie entree = categories.save(new Categorie("Chant d'entrée"));
        categories.save(new Categorie("Noël"));

        chant(1, "Chant du matin", "Chorale de démonstration", "fr", "Ré majeur", louange, """
                Quand le jour se lève sur la ville,
                Nos voix s'unissent en un seul chant.

                Refrain :
                Chantons ensemble, le cœur léger,
                Chantons la joie de ce jour nouveau.""");
        chant(2, "Lumière douce", null, "fr", "Sol majeur", adoration, """
                Lumière douce au fond du soir,
                Tu gardes allumé notre espoir.

                Dans le silence, nous restons là,
                Et ta présence ne s'éteint pas.""");
        chant(3, "Ouvrons les portes", "Chorale de démonstration", "fr", null, entree, """
                Ouvrons les portes, entrons en chantant,
                Petits et grands, venez maintenant.""");
    }

    private void chant(int numero, String titre, String auteur, String langue, String tonalite,
                       Categorie categorie, String paroles) {
        Chant c = new Chant();
        c.setNumero(numero);
        c.setTitre(titre);
        c.setAuteur(auteur);
        c.setLangue(langue);
        c.setTonalite(tonalite);
        c.setCategorie(categorie);
        c.setParoles(paroles);
        chants.save(c);
    }
}
