package com.jodyafanou.chants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/** Test d'intégration : application complète, base H2 en mémoire et données de démo. */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:chants-test;DB_CLOSE_DELAY=-1",
        "app.seed=true"
})
@AutoConfigureMockMvc
class ChantsApplicationTest {

    @Autowired
    MockMvc mvc;

    @Test
    void les_donnees_de_demo_sont_recherchables() throws Exception {
        mvc.perform(get("/api/chants").param("q", "lumière"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.contenu[0].titre").value("Lumière douce"))
                .andExpect(jsonPath("$.contenu[0].categorie.nom").value("Adoration"));

        mvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }
}
