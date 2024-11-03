import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAluno } from 'app/entities/aluno/aluno.model';
import { AlunoService } from 'app/entities/aluno/service/aluno.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';
import { IMatricula } from '../matricula.model';
import { MatriculaService } from '../service/matricula.service';
import { MatriculaFormService } from './matricula-form.service';

import { MatriculaUpdateComponent } from './matricula-update.component';

describe('Matricula Management Update Component', () => {
  let comp: MatriculaUpdateComponent;
  let fixture: ComponentFixture<MatriculaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let matriculaFormService: MatriculaFormService;
  let matriculaService: MatriculaService;
  let alunoService: AlunoService;
  let cursoService: CursoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatriculaUpdateComponent],
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
      .overrideTemplate(MatriculaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MatriculaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    matriculaFormService = TestBed.inject(MatriculaFormService);
    matriculaService = TestBed.inject(MatriculaService);
    alunoService = TestBed.inject(AlunoService);
    cursoService = TestBed.inject(CursoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Aluno query and add missing value', () => {
      const matricula: IMatricula = { id: 456 };
      const aluno: IAluno = { id: 21777 };
      matricula.aluno = aluno;

      const alunoCollection: IAluno[] = [{ id: 18560 }];
      jest.spyOn(alunoService, 'query').mockReturnValue(of(new HttpResponse({ body: alunoCollection })));
      const additionalAlunos = [aluno];
      const expectedCollection: IAluno[] = [...additionalAlunos, ...alunoCollection];
      jest.spyOn(alunoService, 'addAlunoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ matricula });
      comp.ngOnInit();

      expect(alunoService.query).toHaveBeenCalled();
      expect(alunoService.addAlunoToCollectionIfMissing).toHaveBeenCalledWith(
        alunoCollection,
        ...additionalAlunos.map(expect.objectContaining),
      );
      expect(comp.alunosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Curso query and add missing value', () => {
      const matricula: IMatricula = { id: 456 };
      const curso: ICurso = { id: 7329 };
      matricula.curso = curso;

      const cursoCollection: ICurso[] = [{ id: 20001 }];
      jest.spyOn(cursoService, 'query').mockReturnValue(of(new HttpResponse({ body: cursoCollection })));
      const additionalCursos = [curso];
      const expectedCollection: ICurso[] = [...additionalCursos, ...cursoCollection];
      jest.spyOn(cursoService, 'addCursoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ matricula });
      comp.ngOnInit();

      expect(cursoService.query).toHaveBeenCalled();
      expect(cursoService.addCursoToCollectionIfMissing).toHaveBeenCalledWith(
        cursoCollection,
        ...additionalCursos.map(expect.objectContaining),
      );
      expect(comp.cursosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const matricula: IMatricula = { id: 456 };
      const aluno: IAluno = { id: 17195 };
      matricula.aluno = aluno;
      const curso: ICurso = { id: 11661 };
      matricula.curso = curso;

      activatedRoute.data = of({ matricula });
      comp.ngOnInit();

      expect(comp.alunosSharedCollection).toContain(aluno);
      expect(comp.cursosSharedCollection).toContain(curso);
      expect(comp.matricula).toEqual(matricula);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMatricula>>();
      const matricula = { id: 123 };
      jest.spyOn(matriculaFormService, 'getMatricula').mockReturnValue(matricula);
      jest.spyOn(matriculaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matricula });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matricula }));
      saveSubject.complete();

      // THEN
      expect(matriculaFormService.getMatricula).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(matriculaService.update).toHaveBeenCalledWith(expect.objectContaining(matricula));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMatricula>>();
      const matricula = { id: 123 };
      jest.spyOn(matriculaFormService, 'getMatricula').mockReturnValue({ id: null });
      jest.spyOn(matriculaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matricula: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matricula }));
      saveSubject.complete();

      // THEN
      expect(matriculaFormService.getMatricula).toHaveBeenCalled();
      expect(matriculaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMatricula>>();
      const matricula = { id: 123 };
      jest.spyOn(matriculaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matricula });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(matriculaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAluno', () => {
      it('Should forward to alunoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(alunoService, 'compareAluno');
        comp.compareAluno(entity, entity2);
        expect(alunoService.compareAluno).toHaveBeenCalledWith(entity, entity2);
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
  });
});
