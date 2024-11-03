import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { DisciplinaService } from 'app/entities/disciplina/service/disciplina.service';
import { IAluno } from 'app/entities/aluno/aluno.model';
import { AlunoService } from 'app/entities/aluno/service/aluno.service';
import { INota } from '../nota.model';
import { NotaService } from '../service/nota.service';
import { NotaFormService } from './nota-form.service';

import { NotaUpdateComponent } from './nota-update.component';

describe('Nota Management Update Component', () => {
  let comp: NotaUpdateComponent;
  let fixture: ComponentFixture<NotaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notaFormService: NotaFormService;
  let notaService: NotaService;
  let disciplinaService: DisciplinaService;
  let alunoService: AlunoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NotaUpdateComponent],
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
      .overrideTemplate(NotaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notaFormService = TestBed.inject(NotaFormService);
    notaService = TestBed.inject(NotaService);
    disciplinaService = TestBed.inject(DisciplinaService);
    alunoService = TestBed.inject(AlunoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Disciplina query and add missing value', () => {
      const nota: INota = { id: 456 };
      const disciplina: IDisciplina = { id: 20883 };
      nota.disciplina = disciplina;

      const disciplinaCollection: IDisciplina[] = [{ id: 32120 }];
      jest.spyOn(disciplinaService, 'query').mockReturnValue(of(new HttpResponse({ body: disciplinaCollection })));
      const additionalDisciplinas = [disciplina];
      const expectedCollection: IDisciplina[] = [...additionalDisciplinas, ...disciplinaCollection];
      jest.spyOn(disciplinaService, 'addDisciplinaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nota });
      comp.ngOnInit();

      expect(disciplinaService.query).toHaveBeenCalled();
      expect(disciplinaService.addDisciplinaToCollectionIfMissing).toHaveBeenCalledWith(
        disciplinaCollection,
        ...additionalDisciplinas.map(expect.objectContaining),
      );
      expect(comp.disciplinasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Aluno query and add missing value', () => {
      const nota: INota = { id: 456 };
      const aluno: IAluno = { id: 2110 };
      nota.aluno = aluno;

      const alunoCollection: IAluno[] = [{ id: 14569 }];
      jest.spyOn(alunoService, 'query').mockReturnValue(of(new HttpResponse({ body: alunoCollection })));
      const additionalAlunos = [aluno];
      const expectedCollection: IAluno[] = [...additionalAlunos, ...alunoCollection];
      jest.spyOn(alunoService, 'addAlunoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nota });
      comp.ngOnInit();

      expect(alunoService.query).toHaveBeenCalled();
      expect(alunoService.addAlunoToCollectionIfMissing).toHaveBeenCalledWith(
        alunoCollection,
        ...additionalAlunos.map(expect.objectContaining),
      );
      expect(comp.alunosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const nota: INota = { id: 456 };
      const disciplina: IDisciplina = { id: 18372 };
      nota.disciplina = disciplina;
      const aluno: IAluno = { id: 8886 };
      nota.aluno = aluno;

      activatedRoute.data = of({ nota });
      comp.ngOnInit();

      expect(comp.disciplinasSharedCollection).toContain(disciplina);
      expect(comp.alunosSharedCollection).toContain(aluno);
      expect(comp.nota).toEqual(nota);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INota>>();
      const nota = { id: 123 };
      jest.spyOn(notaFormService, 'getNota').mockReturnValue(nota);
      jest.spyOn(notaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nota });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nota }));
      saveSubject.complete();

      // THEN
      expect(notaFormService.getNota).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notaService.update).toHaveBeenCalledWith(expect.objectContaining(nota));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INota>>();
      const nota = { id: 123 };
      jest.spyOn(notaFormService, 'getNota').mockReturnValue({ id: null });
      jest.spyOn(notaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nota: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nota }));
      saveSubject.complete();

      // THEN
      expect(notaFormService.getNota).toHaveBeenCalled();
      expect(notaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INota>>();
      const nota = { id: 123 };
      jest.spyOn(notaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nota });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notaService.update).toHaveBeenCalled();
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
