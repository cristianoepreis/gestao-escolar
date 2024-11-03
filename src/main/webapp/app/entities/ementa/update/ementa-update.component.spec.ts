import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProfessor } from 'app/entities/professor/professor.model';
import { ProfessorService } from 'app/entities/professor/service/professor.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';
import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { DisciplinaService } from 'app/entities/disciplina/service/disciplina.service';
import { IEmenta } from '../ementa.model';
import { EmentaService } from '../service/ementa.service';
import { EmentaFormService } from './ementa-form.service';

import { EmentaUpdateComponent } from './ementa-update.component';

describe('Ementa Management Update Component', () => {
  let comp: EmentaUpdateComponent;
  let fixture: ComponentFixture<EmentaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ementaFormService: EmentaFormService;
  let ementaService: EmentaService;
  let professorService: ProfessorService;
  let cursoService: CursoService;
  let disciplinaService: DisciplinaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmentaUpdateComponent],
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
      .overrideTemplate(EmentaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmentaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ementaFormService = TestBed.inject(EmentaFormService);
    ementaService = TestBed.inject(EmentaService);
    professorService = TestBed.inject(ProfessorService);
    cursoService = TestBed.inject(CursoService);
    disciplinaService = TestBed.inject(DisciplinaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Professor query and add missing value', () => {
      const ementa: IEmenta = { id: 456 };
      const professor: IProfessor = { id: 10520 };
      ementa.professor = professor;

      const professorCollection: IProfessor[] = [{ id: 26396 }];
      jest.spyOn(professorService, 'query').mockReturnValue(of(new HttpResponse({ body: professorCollection })));
      const additionalProfessors = [professor];
      const expectedCollection: IProfessor[] = [...additionalProfessors, ...professorCollection];
      jest.spyOn(professorService, 'addProfessorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ementa });
      comp.ngOnInit();

      expect(professorService.query).toHaveBeenCalled();
      expect(professorService.addProfessorToCollectionIfMissing).toHaveBeenCalledWith(
        professorCollection,
        ...additionalProfessors.map(expect.objectContaining),
      );
      expect(comp.professorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Curso query and add missing value', () => {
      const ementa: IEmenta = { id: 456 };
      const curso: ICurso = { id: 6323 };
      ementa.curso = curso;

      const cursoCollection: ICurso[] = [{ id: 15274 }];
      jest.spyOn(cursoService, 'query').mockReturnValue(of(new HttpResponse({ body: cursoCollection })));
      const additionalCursos = [curso];
      const expectedCollection: ICurso[] = [...additionalCursos, ...cursoCollection];
      jest.spyOn(cursoService, 'addCursoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ementa });
      comp.ngOnInit();

      expect(cursoService.query).toHaveBeenCalled();
      expect(cursoService.addCursoToCollectionIfMissing).toHaveBeenCalledWith(
        cursoCollection,
        ...additionalCursos.map(expect.objectContaining),
      );
      expect(comp.cursosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Disciplina query and add missing value', () => {
      const ementa: IEmenta = { id: 456 };
      const disciplina: IDisciplina = { id: 12464 };
      ementa.disciplina = disciplina;

      const disciplinaCollection: IDisciplina[] = [{ id: 726 }];
      jest.spyOn(disciplinaService, 'query').mockReturnValue(of(new HttpResponse({ body: disciplinaCollection })));
      const additionalDisciplinas = [disciplina];
      const expectedCollection: IDisciplina[] = [...additionalDisciplinas, ...disciplinaCollection];
      jest.spyOn(disciplinaService, 'addDisciplinaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ementa });
      comp.ngOnInit();

      expect(disciplinaService.query).toHaveBeenCalled();
      expect(disciplinaService.addDisciplinaToCollectionIfMissing).toHaveBeenCalledWith(
        disciplinaCollection,
        ...additionalDisciplinas.map(expect.objectContaining),
      );
      expect(comp.disciplinasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ementa: IEmenta = { id: 456 };
      const professor: IProfessor = { id: 6389 };
      ementa.professor = professor;
      const curso: ICurso = { id: 23209 };
      ementa.curso = curso;
      const disciplina: IDisciplina = { id: 31155 };
      ementa.disciplina = disciplina;

      activatedRoute.data = of({ ementa });
      comp.ngOnInit();

      expect(comp.professorsSharedCollection).toContain(professor);
      expect(comp.cursosSharedCollection).toContain(curso);
      expect(comp.disciplinasSharedCollection).toContain(disciplina);
      expect(comp.ementa).toEqual(ementa);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmenta>>();
      const ementa = { id: 123 };
      jest.spyOn(ementaFormService, 'getEmenta').mockReturnValue(ementa);
      jest.spyOn(ementaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ementa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ementa }));
      saveSubject.complete();

      // THEN
      expect(ementaFormService.getEmenta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ementaService.update).toHaveBeenCalledWith(expect.objectContaining(ementa));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmenta>>();
      const ementa = { id: 123 };
      jest.spyOn(ementaFormService, 'getEmenta').mockReturnValue({ id: null });
      jest.spyOn(ementaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ementa: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ementa }));
      saveSubject.complete();

      // THEN
      expect(ementaFormService.getEmenta).toHaveBeenCalled();
      expect(ementaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmenta>>();
      const ementa = { id: 123 };
      jest.spyOn(ementaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ementa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ementaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProfessor', () => {
      it('Should forward to professorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(professorService, 'compareProfessor');
        comp.compareProfessor(entity, entity2);
        expect(professorService.compareProfessor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCurso', () => {
      it('Should forward to cursoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cursoService, 'compareCurso');
        comp.compareCurso(entity, entity2);
        expect(cursoService.compareCurso).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
