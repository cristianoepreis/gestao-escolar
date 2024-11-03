import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MatriculaDetailComponent } from './matricula-detail.component';

describe('Matricula Management Detail Component', () => {
  let comp: MatriculaDetailComponent;
  let fixture: ComponentFixture<MatriculaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatriculaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./matricula-detail.component').then(m => m.MatriculaDetailComponent),
              resolve: { matricula: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MatriculaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatriculaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load matricula on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MatriculaDetailComponent);

      // THEN
      expect(instance.matricula()).toEqual(expect.objectContaining({ id: 123 }));
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
