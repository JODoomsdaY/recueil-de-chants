import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { ChantList } from './chant-list';
import { ChantService } from './chant.service';

describe('ChantList', () => {
  const service = {
    categories: vi.fn(() => of([{ id: 1, nom: 'Louange' }])),
    rechercher: vi.fn(() =>
      of({
        contenu: [
          {
            id: 1,
            numero: 3,
            titre: 'Chant du matin',
            auteur: 'Chorale Sainte-Cécile',
            langue: 'fr',
            categorie: { id: 1, nom: 'Louange' },
          },
        ],
        page: 0,
        taille: 20,
        totalElements: 1,
        totalPages: 1,
      }),
    ),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChantList],
      providers: [provideRouter([]), { provide: ChantService, useValue: service }],
    }).compileComponents();
  });

  it('affiche les chants renvoyés par l’API', async () => {
    const fixture = TestBed.createComponent(ChantList);
    await fixture.whenStable();

    const el = fixture.nativeElement as HTMLElement;
    expect(el.textContent).toContain('1 chant(s)');
    expect(el.querySelector('.chant-list strong')?.textContent).toContain('Chant du matin');
    expect(el.textContent).toContain('Louange');
    expect(service.rechercher).toHaveBeenCalledWith(expect.objectContaining({ page: 0 }));
  });
});
