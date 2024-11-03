import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAluno } from 'app/entities/aluno/aluno.model';
import { AlunoService } from 'app/entities/aluno/service/aluno.service';
import { ICurso } from '../curso.model';
import { CursoService } from '../service/curso.service';
import { CursoFormGroup, CursoFormService } from './curso-form.service';

@Component({
  standalone: true,
  selector: 'jhi-curso-update',
  templateUrl: './curso-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CursoUpdateComponent implements OnInit {
  isSaving = false;
  curso: ICurso | null = null;

  alunosSharedCollection: IAluno[] = [];

  protected cursoService = inject(CursoService);
  protected cursoFormService = inject(CursoFormService);
  protected alunoService = inject(AlunoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CursoFormGroup = this.cursoFormService.createCursoFormGroup();

  compareAluno = (o1: IAluno | null, o2: IAluno | null): boolean => this.alunoService.compareAluno(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ curso }) => {
      this.curso = curso;
      if (curso) {
        this.updateForm(curso);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const curso = this.cursoFormService.getCurso(this.editForm);
    if (curso.id !== null) {
      this.subscribeToSaveResponse(this.cursoService.update(curso));
    } else {
      this.subscribeToSaveResponse(this.cursoService.create(curso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurso>>): void {
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

  protected updateForm(curso: ICurso): void {
    this.curso = curso;
    this.cursoFormService.resetForm(this.editForm, curso);

    this.alunosSharedCollection = this.alunoService.addAlunoToCollectionIfMissing<IAluno>(
      this.alunosSharedCollection,
      ...(curso.alunos ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alunoService
      .query()
      .pipe(map((res: HttpResponse<IAluno[]>) => res.body ?? []))
      .pipe(map((alunos: IAluno[]) => this.alunoService.addAlunoToCollectionIfMissing<IAluno>(alunos, ...(this.curso?.alunos ?? []))))
      .subscribe((alunos: IAluno[]) => (this.alunosSharedCollection = alunos));
  }
}
