import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { DisciplinaService } from 'app/entities/disciplina/service/disciplina.service';
import { ProfessorService } from '../service/professor.service';
import { IProfessor } from '../professor.model';
import { ProfessorFormService } from './professor-form.service';

import { ProfessorUpdateComponent } from './professor-update.component';

describe('Professor Management Update Component', () => {
  let comp: ProfessorUpdateComponent;
  let fixture: ComponentFixture<ProfessorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let professorFormService: ProfessorFormService;
  let professorService: ProfessorService;
  let disciplinaService: DisciplinaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProfessorUpdateComponent],
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
      .overrideTemplate(ProfessorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProfessorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    professorFormService = TestBed.inject(ProfessorFormService);
    professorService = TestBed.inject(ProfessorService);
    disciplinaService = TestBed.inject(DisciplinaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Disciplina query and add missing value', () => {
      const professor: IProfessor = { id: 456 };
      const disciplinas: IDisciplina[] = [{ id: 32467 }];
      professor.disciplinas = disciplinas;

      const disciplinaCollection: IDisciplina[] = [{ id: 3556 }];
      jest.spyOn(disciplinaService, 'query').mockReturnValue(of(new HttpResponse({ body: disciplinaCollection })));
      const additionalDisciplinas = [...disciplinas];
      const expectedCollection: IDisciplina[] = [...additionalDisciplinas, ...disciplinaCollection];
      jest.spyOn(disciplinaService, 'addDisciplinaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ professor });
      comp.ngOnInit();

      expect(disciplinaService.query).toHaveBeenCalled();
      expect(disciplinaService.addDisciplinaToCollectionIfMissing).toHaveBeenCalledWith(
        disciplinaCollection,
        ...additionalDisciplinas.map(expect.objectContaining),
      );
      expect(comp.disciplinasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const professor: IProfessor = { id: 456 };
      const disciplina: IDisciplina = { id: 22346 };
      professor.disciplinas = [disciplina];

      activatedRoute.data = of({ professor });
      comp.ngOnInit();

      expect(comp.disciplinasSharedCollection).toContain(disciplina);
      expect(comp.professor).toEqual(professor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfessor>>();
      const professor = { id: 123 };
      jest.spyOn(professorFormService, 'getProfessor').mockReturnValue(professor);
      jest.spyOn(professorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ professor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: professor }));
      saveSubject.complete();

      // THEN
      expect(professorFormService.getProfessor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(professorService.update).toHaveBeenCalledWith(expect.objectContaining(professor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfessor>>();
      const professor = { id: 123 };
      jest.spyOn(professorFormService, 'getProfessor').mockReturnValue({ id: null });
      jest.spyOn(professorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ professor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: professor }));
      saveSubject.complete();

      // THEN
      expect(professorFormService.getProfessor).toHaveBeenCalled();
      expect(professorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfessor>>();
      const professor = { id: 123 };
      jest.spyOn(professorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ professor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(professorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDisciplina', () => {
      it('Should forward to disciplinaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(disciplinaService, 'compareDisciplina');
        comp.compareDisciplina(entity, entity2);
        expect(disciplinaService.compareDisciplina).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
