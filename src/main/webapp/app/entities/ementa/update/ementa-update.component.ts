import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProfessor } from 'app/entities/professor/professor.model';
import { ProfessorService } from 'app/entities/professor/service/professor.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';
import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { DisciplinaService } from 'app/entities/disciplina/service/disciplina.service';
import { EmentaService } from '../service/ementa.service';
import { IEmenta } from '../ementa.model';
import { EmentaFormGroup, EmentaFormService } from './ementa-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ementa-update',
  templateUrl: './ementa-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmentaUpdateComponent implements OnInit {
  isSaving = false;
  ementa: IEmenta | null = null;

  professorsSharedCollection: IProfessor[] = [];
  cursosSharedCollection: ICurso[] = [];
  disciplinasSharedCollection: IDisciplina[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected ementaService = inject(EmentaService);
  protected ementaFormService = inject(EmentaFormService);
  protected professorService = inject(ProfessorService);
  protected cursoService = inject(CursoService);
  protected disciplinaService = inject(DisciplinaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmentaFormGroup = this.ementaFormService.createEmentaFormGroup();

  compareProfessor = (o1: IProfessor | null, o2: IProfessor | null): boolean => this.professorService.compareProfessor(o1, o2);

  compareCurso = (o1: ICurso | null, o2: ICurso | null): boolean => this.cursoService.compareCurso(o1, o2);

  compareDisciplina = (o1: IDisciplina | null, o2: IDisciplina | null): boolean => this.disciplinaService.compareDisciplina(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ementa }) => {
      this.ementa = ementa;
      if (ementa) {
        this.updateForm(ementa);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gestaoEscolarApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ementa = this.ementaFormService.getEmenta(this.editForm);
    if (ementa.id !== null) {
      this.subscribeToSaveResponse(this.ementaService.update(ementa));
    } else {
      this.subscribeToSaveResponse(this.ementaService.create(ementa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmenta>>): void {
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

  protected updateForm(ementa: IEmenta): void {
    this.ementa = ementa;
    this.ementaFormService.resetForm(this.editForm, ementa);

    this.professorsSharedCollection = this.professorService.addProfessorToCollectionIfMissing<IProfessor>(
      this.professorsSharedCollection,
      ementa.professor,
    );
    this.cursosSharedCollection = this.cursoService.addCursoToCollectionIfMissing<ICurso>(this.cursosSharedCollection, ementa.curso);
    this.disciplinasSharedCollection = this.disciplinaService.addDisciplinaToCollectionIfMissing<IDisciplina>(
      this.disciplinasSharedCollection,
      ementa.disciplina,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.professorService
      .query()
      .pipe(map((res: HttpResponse<IProfessor[]>) => res.body ?? []))
      .pipe(
        map((professors: IProfessor[]) =>
          this.professorService.addProfessorToCollectionIfMissing<IProfessor>(professors, this.ementa?.professor),
        ),
      )
      .subscribe((professors: IProfessor[]) => (this.professorsSharedCollection = professors));

    this.cursoService
      .query()
      .pipe(map((res: HttpResponse<ICurso[]>) => res.body ?? []))
      .pipe(map((cursos: ICurso[]) => this.cursoService.addCursoToCollectionIfMissing<ICurso>(cursos, this.ementa?.curso)))
      .subscribe((cursos: ICurso[]) => (this.cursosSharedCollection = cursos));

    this.disciplinaService
      .query()
      .pipe(map((res: HttpResponse<IDisciplina[]>) => res.body ?? []))
      .pipe(
        map((disciplinas: IDisciplina[]) =>
          this.disciplinaService.addDisciplinaToCollectionIfMissing<IDisciplina>(disciplinas, this.ementa?.disciplina),
        ),
      )
      .subscribe((disciplinas: IDisciplina[]) => (this.disciplinasSharedCollection = disciplinas));
  }
}
