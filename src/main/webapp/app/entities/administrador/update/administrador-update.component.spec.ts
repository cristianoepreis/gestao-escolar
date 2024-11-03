import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AdministradorService } from '../service/administrador.service';
import { IAdministrador } from '../administrador.model';
import { AdministradorFormService } from './administrador-form.service';

import { AdministradorUpdateComponent } from './administrador-update.component';

describe('Administrador Management Update Component', () => {
  let comp: AdministradorUpdateComponent;
  let fixture: ComponentFixture<AdministradorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let administradorFormService: AdministradorFormService;
  let administradorService: AdministradorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AdministradorUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AdministradorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdministradorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    administradorFormService = TestBed.inject(AdministradorFormService);
    administradorService = TestBed.inject(AdministradorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const administrador: IAdministrador = { id: 456 };

      activatedRoute.data = of({ administrador });
      comp.ngOnInit();

      expect(comp.administrador).toEqual(administrador);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdministrador>>();
      const administrador = { id: 123 };
      jest.spyOn(administradorFormService, 'getAdministrador').mockReturnValue(administrador);
      jest.spyOn(administradorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ administrador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: administrador }));
      saveSubject.complete();

      // THEN
      expect(administradorFormService.getAdministrador).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(administradorService.update).toHaveBeenCalledWith(expect.objectContaining(administrador));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdministrador>>();
      const administrador = { id: 123 };
      jest.spyOn(administradorFormService, 'getAdministrador').mockReturnValue({ id: null });
      jest.spyOn(administradorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ administrador: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: administrador }));
      saveSubject.complete();

      // THEN
      expect(administradorFormService.getAdministrador).toHaveBeenCalled();
      expect(administradorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdministrador>>();
      const administrador = { id: 123 };
      jest.spyOn(administradorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ administrador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(administradorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
