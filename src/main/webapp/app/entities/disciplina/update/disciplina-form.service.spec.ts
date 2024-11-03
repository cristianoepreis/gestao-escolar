import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../disciplina.test-samples';

import { DisciplinaFormService } from './disciplina-form.service';

describe('Disciplina Form Service', () => {
  let service: DisciplinaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DisciplinaFormService);
  });

  describe('Service methods', () => {
    describe('createDisciplinaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDisciplinaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            codigo: expect.any(Object),
            cargaHoraria: expect.any(Object),
            curso: expect.any(Object),
            professors: expect.any(Object),
          }),
        );
      });

      it('passing IDisciplina should create a new form with FormGroup', () => {
        const formGroup = service.createDisciplinaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            codigo: expect.any(Object),
            cargaHoraria: expect.any(Object),
            curso: expect.any(Object),
            professors: expect.any(Object),
          }),
        );
      });
    });

    describe('getDisciplina', () => {
      it('should return NewDisciplina for default Disciplina initial value', () => {
        const formGroup = service.createDisciplinaFormGroup(sampleWithNewData);

        const disciplina = service.getDisciplina(formGroup) as any;

        expect(disciplina).toMatchObject(sampleWithNewData);
      });

      it('should return NewDisciplina for empty Disciplina initial value', () => {
        const formGroup = service.createDisciplinaFormGroup();

        const disciplina = service.getDisciplina(formGroup) as any;

        expect(disciplina).toMatchObject({});
      });

      it('should return IDisciplina', () => {
        const formGroup = service.createDisciplinaFormGroup(sampleWithRequiredData);

        const disciplina = service.getDisciplina(formGroup) as any;

        expect(disciplina).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDisciplina should not enable id FormControl', () => {
        const formGroup = service.createDisciplinaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDisciplina should disable id FormControl', () => {
        const formGroup = service.createDisciplinaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
