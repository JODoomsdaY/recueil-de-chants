package com.jodyafanou.chants.web;

import com.jodyafanou.chants.dto.CategorieDto;
import com.jodyafanou.chants.dto.CategorieRequest;
import com.jodyafanou.chants.service.CategorieService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    private final CategorieService service;

    public CategorieController(CategorieService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategorieDto> lister() {
        return service.lister();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategorieDto creer(@Valid @RequestBody CategorieRequest requete) {
        return service.creer(requete.nom());
    }
}
