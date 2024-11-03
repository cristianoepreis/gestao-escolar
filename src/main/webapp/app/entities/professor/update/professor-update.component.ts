import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { DisciplinaService } from 'app/entities/disciplina/service/disciplina.service';
import { IProfessor } from '../professor.model';
import { ProfessorService } from '../service/professor.service';
import { ProfessorFormGroup, ProfessorFormService } from './professor-form.service';

@Component({
  standalone: true,
  selector: 'jhi-professor-update',
  templateUrl: './professor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProfessorUpdateComponent implements OnInit {
  isSaving = false;
  professor: IProfessor | null = null;

  disciplinasSharedCollection: IDisciplina[] = [];

  protected professorService = inject(ProfessorService);
  protected professorFormService = inject(ProfessorFormService);
  protected disciplinaService = inject(DisciplinaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProfessorFormGroup = this.professorFormService.createProfessorFormGroup();

  compareDisciplina = (o1: IDisciplina | null, o2: IDisciplina | null): boolean => this.disciplinaService.compareDisciplina(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ professor }) => {
      this.professor = professor;
      if (professor) {
        this.updateForm(professor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const professor = this.professorFormService.getProfessor(this.editForm);
    if (professor.id !== null) {
      this.subscribeToSaveResponse(this.professorService.update(professor));
    } else {
      this.subscribeToSaveResponse(this.professorService.create(professor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfessor>>): void {
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

  protected updateForm(professor: IProfessor): void {
    this.professor = professor;
    this.professorFormService.resetForm(this.editForm, professor);

    this.disciplinasSharedCollection = this.disciplinaService.addDisciplinaToCollectionIfMissing<IDisciplina>(
      this.disciplinasSharedCollection,
      ...(professor.disciplinas ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.disciplinaService
      .query()
      .pipe(map((res: HttpResponse<IDisciplina[]>) => res.body ?? []))
      .pipe(
        map((disciplinas: IDisciplina[]) =>
          this.disciplinaService.addDisciplinaToCollectionIfMissing<IDisciplina>(disciplinas, ...(this.professor?.disciplinas ?? [])),
        ),
      )
      .subscribe((disciplinas: IDisciplina[]) => (this.disciplinasSharedCollection = disciplinas));
  }
}
