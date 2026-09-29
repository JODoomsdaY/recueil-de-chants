import { Component, OnInit, inject, input, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

import { Chant, LANGUES } from './chant.model';
import { ChantService } from './chant.service';

@Component({
  selector: 'app-chant-detail',
  imports: [RouterLink],
  templateUrl: './chant-detail.html',
  styleUrl: './chants.scss',
})
export class ChantDetail implements OnInit {
  private readonly service = inject(ChantService);
  private readonly router = inject(Router);

  /** Paramètre de route, injecté grâce à withComponentInputBinding() */
  readonly id = input.required<string>();

  protected readonly chant = signal<Chant | null>(null);
  protected readonly erreur = signal<string | null>(null);
  protected readonly langues = LANGUES;

  ngOnInit(): void {
    this.service.obtenir(Number(this.id())).subscribe({
      next: (c) => this.chant.set(c),
      error: () => this.erreur.set('Chant introuvable.'),
    });
  }

  protected nomLangue(code: string): string {
    return this.langues[code] ?? code;
  }

  protected supprimer(chant: Chant): void {
    if (!confirm(`Supprimer « ${chant.titre} » ?`)) return;
    this.service.supprimer(chant.id).subscribe(() => this.router.navigate(['/chants']));
  }
}
