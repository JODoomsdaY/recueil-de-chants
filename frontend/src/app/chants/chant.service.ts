import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Categorie, Chant, ChantPayload, ChantResume, Page, Recherche } from './chant.model';

@Injectable({ providedIn: 'root' })
export class ChantService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api';

  rechercher(recherche: Recherche = {}): Observable<Page<ChantResume>> {
    let params = new HttpParams()
      .set('page', recherche.page ?? 0)
      .set('taille', recherche.taille ?? 20);
    if (recherche.q?.trim()) params = params.set('q', recherche.q.trim());
    if (recherche.categorieId) params = params.set('categorieId', recherche.categorieId);
    if (recherche.langue) params = params.set('langue', recherche.langue);

    return this.http.get<Page<ChantResume>>(`${this.baseUrl}/chants`, { params });
  }

  obtenir(id: number): Observable<Chant> {
    return this.http.get<Chant>(`${this.baseUrl}/chants/${id}`);
  }

  creer(chant: ChantPayload): Observable<Chant> {
    return this.http.post<Chant>(`${this.baseUrl}/chants`, chant);
  }

  modifier(id: number, chant: ChantPayload): Observable<Chant> {
    return this.http.put<Chant>(`${this.baseUrl}/chants/${id}`, chant);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/chants/${id}`);
  }

  categories(): Observable<Categorie[]> {
    return this.http.get<Categorie[]>(`${this.baseUrl}/categories`);
  }
}
