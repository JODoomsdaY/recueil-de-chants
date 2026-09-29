import { Component, OnInit, inject, input, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { KeyValuePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

import { Categorie, ChantPayload, LANGUES } from './chant.model';
import { ChantService } from './chant.service';

@Component({
  selector: 'app-chant-form',
  imports: [ReactiveFormsModule, RouterLink, KeyValuePipe],
  templateUrl: './chant-form.html',
  styleUrl: './chants.scss',
})
export class ChantForm implements OnInit {
  private readonly service = inject(ChantService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);

  /** Absent en création, présent en modification (/chants/:id/modifier) */
  readonly id = input<string>();

  protected readonly langues = LANGUES;
  protected readonly categories = signal<Categorie[]>([]);
  protected readonly erreur = signal<string | null>(null);
  protected readonly envoi = signal(false);

  protected readonly form = this.fb.group({
    numero: this.fb.control<number | null>(null, [Validators.min(1)]),
    titre: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(200)]),
    auteur: this.fb.control<string | null>(null, [Validators.maxLength(150)]),
    langue: this.fb.nonNullable.control('fr', [Validators.required]),
    tonalite: this.fb.control<string | null>(null, [Validators.maxLength(10)]),
    categorieId: this.fb.control<number | null>(null),
    paroles: this.fb.nonNullable.control('', [Validators.required, Validators.maxLength(10000)]),
  });

  ngOnInit(): void {
    this.service.categories().subscribe((c) => this.categories.set(c));

    const id = this.id();
    if (id) {
      this.service.obtenir(Number(id)).subscribe((c) =>
        this.form.patchValue({ ...c, categorieId: c.categorie?.id ?? null }),
      );
    }
  }

  protected enregistrer(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.envoi.set(true);
    const payload = this.form.getRawValue() as ChantPayload;
    const id = this.id();
    const requete = id ? this.service.modifier(Number(id), payload) : this.service.creer(payload);

    requete.subscribe({
      next: (chant) => this.router.navigate(['/chants', chant.id]),
      error: (e: HttpErrorResponse) => {
        this.erreur.set(e.error?.detail ?? "L'enregistrement a échoué.");
        this.envoi.set(false);
      },
    });
  }

  protected invalide(champ: keyof typeof this.form.controls): boolean {
    const control = this.form.controls[champ];
    return control.invalid && control.touched;
  }
}
