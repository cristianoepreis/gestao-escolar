import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAluno } from 'app/entities/aluno/aluno.model';
import { AlunoService } from 'app/entities/aluno/service/aluno.service';
import { CursoService } from '../service/curso.service';
import { ICurso } from '../curso.model';
import { CursoFormService } from './curso-form.service';

import { CursoUpdateComponent } from './curso-update.component';

describe('Curso Management Update Component', () => {
  let comp: CursoUpdateComponent;
  let fixture: ComponentFixture<CursoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cursoFormService: CursoFormService;
  let cursoService: CursoService;
  let alunoService: AlunoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CursoUpdateComponent],
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
      .overrideTemplate(CursoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CursoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cursoFormService = TestBed.inject(CursoFormService);
    cursoService = TestBed.inject(CursoService);
    alunoService = TestBed.inject(AlunoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Aluno query and add missing value', () => {
      const curso: ICurso = { id: 456 };
      const alunos: IAluno[] = [{ id: 14617 }];
      curso.alunos = alunos;

      const alunoCollection: IAluno[] = [{ id: 5441 }];
      jest.spyOn(alunoService, 'query').mockReturnValue(of(new HttpResponse({ body: alunoCollection })));
      const additionalAlunos = [...alunos];
      const expectedCollection: IAluno[] = [...additionalAlunos, ...alunoCollection];
      jest.spyOn(alunoService, 'addAlunoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      expect(alunoService.query).toHaveBeenCalled();
      expect(alunoService.addAlunoToCollectionIfMissing).toHaveBeenCalledWith(
        alunoCollection,
        ...additionalAlunos.map(expect.objectContaining),
      );
      expect(comp.alunosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const curso: ICurso = { id: 456 };
      const aluno: IAluno = { id: 14999 };
      curso.alunos = [aluno];

      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      expect(comp.alunosSharedCollection).toContain(aluno);
      expect(comp.curso).toEqual(curso);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurso>>();
      const curso = { id: 123 };
      jest.spyOn(cursoFormService, 'getCurso').mockReturnValue(curso);
      jest.spyOn(cursoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curso }));
      saveSubject.complete();

      // THEN
      expect(cursoFormService.getCurso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cursoService.update).toHaveBeenCalledWith(expect.objectContaining(curso));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurso>>();
      const curso = { id: 123 };
      jest.spyOn(cursoFormService, 'getCurso').mockReturnValue({ id: null });
      jest.spyOn(cursoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curso }));
      saveSubject.complete();

      // THEN
      expect(cursoFormService.getCurso).toHaveBeenCalled();
      expect(cursoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurso>>();
      const curso = { id: 123 };
      jest.spyOn(cursoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cursoService.update).toHaveBeenCalled();
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
  });
});
