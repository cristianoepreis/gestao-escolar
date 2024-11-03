import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { DisciplinaService } from 'app/entities/disciplina/service/disciplina.service';
import { IAluno } from 'app/entities/aluno/aluno.model';
import { AlunoService } from 'app/entities/aluno/service/aluno.service';
import { NotaService } from '../service/nota.service';
import { INota } from '../nota.model';
import { NotaFormGroup, NotaFormService } from './nota-form.service';

@Component({
  standalone: true,
  selector: 'jhi-nota-update',
  templateUrl: './nota-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NotaUpdateComponent implements OnInit {
  isSaving = false;
  nota: INota | null = null;

  disciplinasSharedCollection: IDisciplina[] = [];
  alunosSharedCollection: IAluno[] = [];

  protected notaService = inject(NotaService);
  protected notaFormService = inject(NotaFormService);
  protected disciplinaService = inject(DisciplinaService);
  protected alunoService = inject(AlunoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: NotaFormGroup = this.notaFormService.createNotaFormGroup();

  compareDisciplina = (o1: IDisciplina | null, o2: IDisciplina | null): boolean => this.disciplinaService.compareDisciplina(o1, o2);

  compareAluno = (o1: IAluno | null, o2: IAluno | null): boolean => this.alunoService.compareAluno(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nota }) => {
      this.nota = nota;
      if (nota) {
        this.updateForm(nota);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nota = this.notaFormService.getNota(this.editForm);
    if (nota.id !== null) {
      this.subscribeToSaveResponse(this.notaService.update(nota));
    } else {
      this.subscribeToSaveResponse(this.notaService.create(nota));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INota>>): void {
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

  protected updateForm(nota: INota): void {
    this.nota = nota;
    this.notaFormService.resetForm(this.editForm, nota);

    this.disciplinasSharedCollection = this.disciplinaService.addDisciplinaToCollectionIfMissing<IDisciplina>(
      this.disciplinasSharedCollection,
      nota.disciplina,
    );
    this.alunosSharedCollection = this.alunoService.addAlunoToCollectionIfMissing<IAluno>(this.alunosSharedCollection, nota.aluno);
  }

  protected loadRelationshipsOptions(): void {
    this.disciplinaService
      .query()
      .pipe(map((res: HttpResponse<IDisciplina[]>) => res.body ?? []))
      .pipe(
        map((disciplinas: IDisciplina[]) =>
          this.disciplinaService.addDisciplinaToCollectionIfMissing<IDisciplina>(disciplinas, this.nota?.disciplina),
        ),
      )
      .subscribe((disciplinas: IDisciplina[]) => (this.disciplinasSharedCollection = disciplinas));

    this.alunoService
      .query()
      .pipe(map((res: HttpResponse<IAluno[]>) => res.body ?? []))
      .pipe(map((alunos: IAluno[]) => this.alunoService.addAlunoToCollectionIfMissing<IAluno>(alunos, this.nota?.aluno)))
      .subscribe((alunos: IAluno[]) => (this.alunosSharedCollection = alunos));
  }
}
