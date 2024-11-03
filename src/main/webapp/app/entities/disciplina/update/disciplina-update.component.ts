import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';
import { IProfessor } from 'app/entities/professor/professor.model';
import { ProfessorService } from 'app/entities/professor/service/professor.service';
import { DisciplinaService } from '../service/disciplina.service';
import { IDisciplina } from '../disciplina.model';
import { DisciplinaFormGroup, DisciplinaFormService } from './disciplina-form.service';

@Component({
  standalone: true,
  selector: 'jhi-disciplina-update',
  templateUrl: './disciplina-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DisciplinaUpdateComponent implements OnInit {
  isSaving = false;
  disciplina: IDisciplina | null = null;

  cursosSharedCollection: ICurso[] = [];
  professorsSharedCollection: IProfessor[] = [];

  protected disciplinaService = inject(DisciplinaService);
  protected disciplinaFormService = inject(DisciplinaFormService);
  protected cursoService = inject(CursoService);
  protected professorService = inject(ProfessorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DisciplinaFormGroup = this.disciplinaFormService.createDisciplinaFormGroup();

  compareCurso = (o1: ICurso | null, o2: ICurso | null): boolean => this.cursoService.compareCurso(o1, o2);

  compareProfessor = (o1: IProfessor | null, o2: IProfessor | null): boolean => this.professorService.compareProfessor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disciplina }) => {
      this.disciplina = disciplina;
      if (disciplina) {
        this.updateForm(disciplina);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const disciplina = this.disciplinaFormService.getDisciplina(this.editForm);
    if (disciplina.id !== null) {
      this.subscribeToSaveResponse(this.disciplinaService.update(disciplina));
    } else {
      this.subscribeToSaveResponse(this.disciplinaService.create(disciplina));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDisciplina>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(disciplina: IDisciplina): void {
    this.disciplina = disciplina;
    this.disciplinaFormService.resetForm(this.editForm, disciplina);

    this.cursosSharedCollection = this.cursoService.addCursoToCollectionIfMissing<ICurso>(this.cursosSharedCollection, disciplina.curso);
    this.professorsSharedCollection = this.professorService.addProfessorToCollectionIfMissing<IProfessor>(
      this.professorsSharedCollection,
      ...(disciplina.professors ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cursoService
      .query()
      .pipe(map((res: HttpResponse<ICurso[]>) => res.body ?? []))
      .pipe(map((cursos: ICurso[]) => this.cursoService.addCursoToCollectionIfMissing<ICurso>(cursos, this.disciplina?.curso)))
      .subscribe((cursos: ICurso[]) => (this.cursosSharedCollection = cursos));

    this.professorService
      .query()
      .pipe(map((res: HttpResponse<IProfessor[]>) => res.body ?? []))
      .pipe(
        map((professors: IProfessor[]) =>
          this.professorService.addProfessorToCollectionIfMissing<IProfessor>(professors, ...(this.disciplina?.professors ?? [])),
        ),
      )
      .subscribe((professors: IProfessor[]) => (this.professorsSharedCollection = professors));
  }
}
