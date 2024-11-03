import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DisciplinaDetailComponent } from './disciplina-detail.component';

describe('Disciplina Management Detail Component', () => {
  let comp: DisciplinaDetailComponent;
  let fixture: ComponentFixture<DisciplinaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisciplinaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./disciplina-detail.component').then(m => m.DisciplinaDetailComponent),
              resolve: { disciplina: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DisciplinaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DisciplinaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load disciplina on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DisciplinaDetailComponent);

      // THEN
      expect(instance.disciplina()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
