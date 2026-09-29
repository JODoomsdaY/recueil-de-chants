package com.jodyafanou.chants.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jodyafanou.chants.dto.ChantRequest;
import com.jodyafanou.chants.dto.ChantResponse;
import com.jodyafanou.chants.service.ChantService;
import com.jodyafanou.chants.service.ConflitException;
import com.jodyafanou.chants.service.RessourceIntrouvableException;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChantController.class)
class ChantControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    ChantService service;

    @Test
    void cree_un_chant_et_renvoie_201_avec_location() throws Exception {
        when(service.creer(any(ChantRequest.class))).thenReturn(new ChantResponse(
                7L, 12, "Chant du matin", null, "fr", null, "Paroles", null, Instant.now(), Instant.now()));

        mvc.perform(post("/api/chants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero": 12, "titre": "Chant du matin", "langue": "fr", "paroles": "Paroles"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/chants/7"))
                .andExpect(jsonPath("$.titre").value("Chant du matin"));
    }

    @Test
    void renvoie_400_avec_le_detail_des_champs_invalides() throws Exception {
        mvc.perform(post("/api/chants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero": -1, "titre": "", "langue": "fr"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.champs.titre").exists())
                .andExpect(jsonPath("$.champs.paroles").exists())
                .andExpect(jsonPath("$.champs.numero").exists());
    }

    @Test
    void renvoie_404_au_format_problem_json() throws Exception {
        when(service.obtenir(eq(42L))).thenThrow(new RessourceIntrouvableException("Chant 42 introuvable."));

        mvc.perform(get("/api/chants/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Chant 42 introuvable."));
    }

    @Test
    void renvoie_409_sur_un_numero_en_double() throws Exception {
        when(service.creer(any(ChantRequest.class))).thenThrow(new ConflitException("Le numéro 1 est déjà utilisé."));

        mvc.perform(post("/api/chants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero": 1, "titre": "Doublon", "langue": "fr", "paroles": "…"}
                                """))
                .andExpect(status().isConflict());
    }
}
