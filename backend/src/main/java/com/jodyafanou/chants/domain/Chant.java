package com.jodyafanou.chants.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(indexes = {
        @Index(name = "idx_chant_titre", columnList = "titre"),
        @Index(name = "idx_chant_numero", columnList = "numero", unique = true)
})
public class Chant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Numéro du chant dans le recueil papier (facultatif, unique) */
    private Integer numero;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(length = 150)
    private String auteur;

    /** Code de langue : fr, ee (éwé), mina, en, la… */
    @Column(nullable = false, length = 10)
    private String langue;

    @Column(length = 20)
    private String tonalite;

    @Column(nullable = false, length = 10_000)
    private String paroles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @Column(nullable = false, updatable = false)
    private Instant creeLe;

    @Column(nullable = false)
    private Instant modifieLe;

    @PrePersist
    void avantCreation() {
        creeLe = Instant.now();
        modifieLe = creeLe;
    }

    @PreUpdate
    void avantModification() {
        modifieLe = Instant.now();
    }

    public Long getId() { return id; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public String getLangue() { return langue; }
    public void setLangue(String langue) { this.langue = langue; }
    public String getTonalite() { return tonalite; }
    public void setTonalite(String tonalite) { this.tonalite = tonalite; }
    public String getParoles() { return paroles; }
    public void setParoles(String paroles) { this.paroles = paroles; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public Instant getCreeLe() { return creeLe; }
    public Instant getModifieLe() { return modifieLe; }
}
