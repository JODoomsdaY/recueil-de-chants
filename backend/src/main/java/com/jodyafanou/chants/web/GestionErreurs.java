package com.jodyafanou.chants.web;

import com.jodyafanou.chants.service.ConflitException;
import com.jodyafanou.chants.service.RessourceIntrouvableException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** Erreurs au format standard RFC 9457 (application/problem+json). */
@RestControllerAdvice
public class GestionErreurs extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RessourceIntrouvableException.class)
    ProblemDetail introuvable(RessourceIntrouvableException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ConflitException.class)
    ProblemDetail conflit(ConflitException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    /** Ajoute le détail des champs invalides à la réponse 400. */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        Map<String, String> champs = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> champs.putIfAbsent(err.getField(), err.getDefaultMessage()));

        ProblemDetail probleme = ProblemDetail.forStatusAndDetail(status, "Données invalides.");
        probleme.setProperty("champs", champs);
        return ResponseEntity.status(status).body(probleme);
    }
}
