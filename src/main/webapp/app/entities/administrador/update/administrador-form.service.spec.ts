import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../administrador.test-samples';

import { AdministradorFormService } from './administrador-form.service';

describe('Administrador Form Service', () => {
  let service: AdministradorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdministradorFormService);
  });

  describe('Service methods', () => {
    describe('createAdministradorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAdministradorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            telefone: expect.any(Object),
            email: expect.any(Object),
          }),
        );
      });

      it('passing IAdministrador should create a new form with FormGroup', () => {
        const formGroup = service.createAdministradorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            telefone: expect.any(Object),
            email: expect.any(Object),
          }),
        );
      });
    });

    describe('getAdministrador', () => {
      it('should return NewAdministrador for default Administrador initial value', () => {
        const formGroup = service.createAdministradorFormGroup(sampleWithNewData);

        const administrador = service.getAdministrador(formGroup) as any;

        expect(administrador).toMatchObject(sampleWithNewData);
      });

      it('should return NewAdministrador for empty Administrador initial value', () => {
        const formGroup = service.createAdministradorFormGroup();

        const administrador = service.getAdministrador(formGroup) as any;

        expect(administrador).toMatchObject({});
      });

      it('should return IAdministrador', () => {
        const formGroup = service.createAdministradorFormGroup(sampleWithRequiredData);

        const administrador = service.getAdministrador(formGroup) as any;

        expect(administrador).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAdministrador should not enable id FormControl', () => {
        const formGroup = service.createAdministradorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAdministrador should disable id FormControl', () => {
        const formGroup = service.createAdministradorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
