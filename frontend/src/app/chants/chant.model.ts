export interface Categorie {
  id: number;
  nom: string;
}

export interface ChantResume {
  id: number;
  numero: number | null;
  titre: string;
  auteur: string | null;
  langue: string;
  categorie: Categorie | null;
}

export interface Chant extends ChantResume {
  tonalite: string | null;
  paroles: string;
  creeLe: string;
  modifieLe: string;
}

export interface ChantPayload {
  numero: number | null;
  titre: string;
  auteur: string | null;
  langue: string;
  tonalite: string | null;
  paroles: string;
  categorieId: number | null;
}

export interface Page<T> {
  contenu: T[];
  page: number;
  taille: number;
  totalElements: number;
  totalPages: number;
}

export interface Recherche {
  q?: string;
  categorieId?: number | null;
  langue?: string | null;
  page?: number;
  taille?: number;
}

export const LANGUES: Record<string, string> = {
  fr: 'Français',
  ee: 'Éwé',
  mina: 'Mina',
  en: 'Anglais',
  la: 'Latin',
};
