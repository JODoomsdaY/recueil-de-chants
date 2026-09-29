import { KeyValuePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { Categorie, ChantResume, LANGUES, Page } from './chant.model';
import { ChantService } from './chant.service';

@Component({
  selector: 'app-chant-list',
  imports: [FormsModule, RouterLink, KeyValuePipe],
  templateUrl: './chant-list.html',
  styleUrl: './chants.scss',
})
export class ChantList implements OnInit {
  private readonly service = inject(ChantService);
  private readonly saisie$ = new Subject<string>();

  protected readonly langues = LANGUES;
  protected readonly categories = signal<Categorie[]>([]);
  protected readonly resultats = signal<Page<ChantResume> | null>(null);
  protected readonly chargement = signal(false);
  protected readonly erreur = signal<string | null>(null);

  protected q = '';
  protected categorieId: number | null = null;
  protected langue: string | null = null;
  protected page = 0;

  constructor() {
    this.saisie$
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntilDestroyed())
      .subscribe(() => this.rechercher(0));
  }

  ngOnInit(): void {
    this.service.categories().subscribe({ next: (c) => this.categories.set(c) });
    this.rechercher(0);
  }

  protected onSaisie(valeur: string): void {
    this.q = valeur;
    this.saisie$.next(valeur);
  }

  protected rechercher(page = this.page): void {
    this.page = page;
    this.chargement.set(true);
    this.erreur.set(null);
    this.service
      .rechercher({ q: this.q, categorieId: this.categorieId, langue: this.langue, page })
      .subscribe({
        next: (resultats) => {
          this.resultats.set(resultats);
          this.chargement.set(false);
        },
        error: () => {
          this.erreur.set("Impossible de joindre l'API. Le backend est-il lancé ?");
          this.chargement.set(false);
        },
      });
  }

  protected nomLangue(code: string): string {
    return this.langues[code] ?? code;
  }
}
