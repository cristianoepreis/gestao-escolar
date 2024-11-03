import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ementa.test-samples';

import { EmentaFormService } from './ementa-form.service';

describe('Ementa Form Service', () => {
  let service: EmentaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmentaFormService);
  });

  describe('Service methods', () => {
    describe('createEmentaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmentaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descricao: expect.any(Object),
            bibliografiaBasica: expect.any(Object),
            bibliografiaComplementar: expect.any(Object),
            praticaLaboratorial: expect.any(Object),
            professor: expect.any(Object),
            curso: expect.any(Object),
            disciplina: expect.any(Object),
          }),
        );
      });

      it('passing IEmenta should create a new form with FormGroup', () => {
        const formGroup = service.createEmentaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descricao: expect.any(Object),
            bibliografiaBasica: expect.any(Object),
            bibliografiaComplementar: expect.any(Object),
            praticaLaboratorial: expect.any(Object),
            professor: expect.any(Object),
            curso: expect.any(Object),
            disciplina: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmenta', () => {
      it('should return NewEmenta for default Ementa initial value', () => {
        const formGroup = service.createEmentaFormGroup(sampleWithNewData);

        const ementa = service.getEmenta(formGroup) as any;

        expect(ementa).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmenta for empty Ementa initial value', () => {
        const formGroup = service.createEmentaFormGroup();

        const ementa = service.getEmenta(formGroup) as any;

        expect(ementa).toMatchObject({});
      });

      it('should return IEmenta', () => {
        const formGroup = service.createEmentaFormGroup(sampleWithRequiredData);

        const ementa = service.getEmenta(formGroup) as any;

        expect(ementa).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmenta should not enable id FormControl', () => {
        const formGroup = service.createEmentaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmenta should disable id FormControl', () => {
        const formGroup = service.createEmentaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
