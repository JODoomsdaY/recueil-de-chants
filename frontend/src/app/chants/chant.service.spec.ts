import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { ChantService } from './chant.service';

describe('ChantService', () => {
  let service: ChantService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ChantService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it("n'envoie que les filtres renseignés", () => {
    service.rechercher({ q: '  lumière ', categorieId: 2, langue: null, page: 1 }).subscribe();

    const req = http.expectOne((r) => r.url === '/api/chants');
    expect(req.request.params.get('q')).toBe('lumière');
    expect(req.request.params.get('categorieId')).toBe('2');
    expect(req.request.params.has('langue')).toBe(false);
    expect(req.request.params.get('page')).toBe('1');
    req.flush({ contenu: [], page: 1, taille: 20, totalElements: 0, totalPages: 0 });
  });

  it('crée un chant en POST', () => {
    const payload = {
      numero: 12,
      titre: 'Chant du matin',
      auteur: null,
      langue: 'fr',
      tonalite: null,
      paroles: 'Couplet 1',
      categorieId: null,
    };
    let id: number | undefined;
    service.creer(payload).subscribe((c) => (id = c.id));

    const req = http.expectOne('/api/chants');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush({ ...payload, id: 42 });

    expect(id).toBe(42);
  });
});
