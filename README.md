# 🎵 Recueil de chants — Spring Boot + Angular

Application de **numérisation d'un recueil de chants** : une **API REST Spring Boot** pour gérer les chants (titre, auteur, langue, tonalité, paroles, catégorie) avec une recherche plein texte, et une interface **Angular** pour les consulter, les rechercher et les éditer.

![Backend](https://github.com/JODoomsdaY/recueil-de-chants/actions/workflows/backend.yml/badge.svg)
![Frontend](https://github.com/JODoomsdaY/recueil-de-chants/actions/workflows/frontend.yml/badge.svg)

> Projet personnel inspiré de mon stage chez ABZ Technologies (Lomé, 2023), où j'ai développé une application de numérisation de chants avec Spring Boot, Angular et MySQL. Le code de ce dépôt est entièrement réécrit ; les chants d'exemple ont été rédigés pour le projet.

---

## Architecture

```
┌────────────────────┐   /api (proxy)   ┌─────────────────────────────────────┐     ┌────────────┐
│ Angular 21         │ ───────────────► │ Spring Boot 3.5 · Java 21           │ ──► │ H2 / MySQL │
│ standalone,signals │                  │ Controller → Service → Repository   │     └────────────┘
└────────────────────┘                  │ DTO (records) · Specifications JPA  │
                                        └─────────────────────────────────────┘
```

| Dossier | Contenu |
|---|---|
| [`backend/`](backend) | API Spring Boot · Spring Data JPA · Bean Validation · OpenAPI (Swagger) |
| [`frontend/`](frontend) | Angular 21 · composants standalone · signals · formulaires réactifs · Vitest |

## Fonctionnalités

- Recherche **insensible à la casse** dans le titre, l'auteur et les paroles
- Filtres par **catégorie** (louange, adoration, Noël…) et par **langue** (français, éwé, mina, anglais, latin)
- Pagination, tri par numéro du recueil
- Création, modification et suppression des chants, avec validation côté client et côté serveur
- Numéro de recueil unique (erreur **409** en cas de doublon)
- Erreurs au format standard **Problem Details (RFC 9457)**, avec le détail des champs invalides

## API

| Méthode | Route | Description |
|---|---|---|
| `GET` | `/api/chants?q=&categorieId=&langue=&page=0&taille=20` | Recherche paginée |
| `GET` | `/api/chants/{id}` | Détail d'un chant avec ses paroles |
| `POST` | `/api/chants` | Création |
| `PUT` | `/api/chants/{id}` | Modification |
| `DELETE` | `/api/chants/{id}` | Suppression |
| `GET` `POST` | `/api/categories` | Liste et création des catégories |

Documentation interactive : **http://localhost:8080/swagger-ui.html**

## Lancer le projet

**Backend** (Java 21, Maven)

```bash
cd backend
mvn spring-boot:run                                        # H2, données de démo chargées
mvn spring-boot:run -Dspring-boot.run.profiles=mysql       # MySQL (variables DB_HOST, DB_NAME, DB_USER, DB_PASSWORD)
```

**Frontend** (Node 22)

```bash
cd frontend
npm install
npm start          # http://localhost:4200, les appels /api sont redirigés vers le backend
```

## Tests

```bash
cd backend && mvn verify                 # service (@DataJpaTest), contrôleur (@WebMvcTest), intégration
cd frontend && npx ng test --watch=false # service HTTP et composants (Vitest)
```

Les deux suites tournent automatiquement à chaque push via GitHub Actions.

## Choix techniques

- **DTO en `record`** : l'API n'expose jamais directement les entités JPA.
- **Specifications JPA** combinables pour les filtres, et `@EntityGraph` pour charger la catégorie sans requêtes N+1.
- **Liste sans les paroles** (`ChantResume`) pour alléger les réponses paginées.
- **Format de pagination maison** (`PageResponse`) plutôt que la sérialisation de `Page`, qui n'est pas stable.
- Côté Angular : **composants standalone, signals** et liaison des paramètres de route en `input()`.

---

Réalisé par **Joseph-Bernardin Afanou** · [LinkedIn](https://www.linkedin.com/in/joseph-bernardin-afanou)
