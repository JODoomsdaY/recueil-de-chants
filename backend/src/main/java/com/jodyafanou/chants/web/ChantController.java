package com.jodyafanou.chants.web;

import com.jodyafanou.chants.dto.ChantRequest;
import com.jodyafanou.chants.dto.ChantResponse;
import com.jodyafanou.chants.dto.ChantResume;
import com.jodyafanou.chants.dto.PageResponse;
import com.jodyafanou.chants.service.ChantService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chants")
public class ChantController {

    private final ChantService service;

    public ChantController(ChantService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<ChantResume> rechercher(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categorieId,
            @RequestParam(required = false) String langue,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int taille) {
        return service.rechercher(q, categorieId, langue, page, taille);
    }

    @GetMapping("/{id}")
    public ChantResponse obtenir(@PathVariable Long id) {
        return service.obtenir(id);
    }

    @PostMapping
    public ResponseEntity<ChantResponse> creer(@Valid @RequestBody ChantRequest requete) {
        ChantResponse chant = service.creer(requete);
        return ResponseEntity.created(URI.create("/api/chants/" + chant.id())).body(chant);
    }

    @PutMapping("/{id}")
    public ChantResponse modifier(@PathVariable Long id, @Valid @RequestBody ChantRequest requete) {
        return service.modifier(id, requete);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
