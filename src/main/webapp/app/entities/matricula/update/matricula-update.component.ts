import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAluno } from 'app/entities/aluno/aluno.model';
import { AlunoService } from 'app/entities/aluno/service/aluno.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';
import { StatusMatricula } from 'app/entities/enumerations/status-matricula.model';
import { MatriculaService } from '../service/matricula.service';
import { IMatricula } from '../matricula.model';
import { MatriculaFormGroup, MatriculaFormService } from './matricula-form.service';

@Component({
  standalone: true,
  selector: 'jhi-matricula-update',
  templateUrl: './matricula-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MatriculaUpdateComponent implements OnInit {
  isSaving = false;
  matricula: IMatricula | null = null;
  statusMatriculaValues = Object.keys(StatusMatricula);

  alunosSharedCollection: IAluno[] = [];
  cursosSharedCollection: ICurso[] = [];

  protected matriculaService = inject(MatriculaService);
  protected matriculaFormService = inject(MatriculaFormService);
  protected alunoService = inject(AlunoService);
  protected cursoService = inject(CursoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MatriculaFormGroup = this.matriculaFormService.createMatriculaFormGroup();

  compareAluno = (o1: IAluno | null, o2: IAluno | null): boolean => this.alunoService.compareAluno(o1, o2);

  compareCurso = (o1: ICurso | null, o2: ICurso | null): boolean => this.cursoService.compareCurso(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matricula }) => {
      this.matricula = matricula;
      if (matricula) {
        this.updateForm(matricula);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matricula = this.matriculaFormService.getMatricula(this.editForm);
    if (matricula.id !== null) {
      this.subscribeToSaveResponse(this.matriculaService.update(matricula));
    } else {
      this.subscribeToSaveResponse(this.matriculaService.create(matricula));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatricula>>): void {
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

  protected updateForm(matricula: IMatricula): void {
    this.matricula = matricula;
    this.matriculaFormService.resetForm(this.editForm, matricula);

    this.alunosSharedCollection = this.alunoService.addAlunoToCollectionIfMissing<IAluno>(this.alunosSharedCollection, matricula.aluno);
    this.cursosSharedCollection = this.cursoService.addCursoToCollectionIfMissing<ICurso>(this.cursosSharedCollection, matricula.curso);
  }

  protected loadRelationshipsOptions(): void {
    this.alunoService
      .query()
      .pipe(map((res: HttpResponse<IAluno[]>) => res.body ?? []))
      .pipe(map((alunos: IAluno[]) => this.alunoService.addAlunoToCollectionIfMissing<IAluno>(alunos, this.matricula?.aluno)))
      .subscribe((alunos: IAluno[]) => (this.alunosSharedCollection = alunos));

    this.cursoService
      .query()
      .pipe(map((res: HttpResponse<ICurso[]>) => res.body ?? []))
      .pipe(map((cursos: ICurso[]) => this.cursoService.addCursoToCollectionIfMissing<ICurso>(cursos, this.matricula?.curso)))
      .subscribe((cursos: ICurso[]) => (this.cursosSharedCollection = cursos));
  }
}
