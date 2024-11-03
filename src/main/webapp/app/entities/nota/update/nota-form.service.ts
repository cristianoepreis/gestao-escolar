import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { INota, NewNota } from '../nota.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INota for edit and NewNotaFormGroupInput for create.
 */
type NotaFormGroupInput = INota | PartialWithRequiredKeyOf<NewNota>;

type NotaFormDefaults = Pick<NewNota, 'id'>;

type NotaFormGroupContent = {
  id: FormControl<INota['id'] | NewNota['id']>;
  pontuacao: FormControl<INota['pontuacao']>;
  data: FormControl<INota['data']>;
  disciplina: FormControl<INota['disciplina']>;
  aluno: FormControl<INota['aluno']>;
};

export type NotaFormGroup = FormGroup<NotaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotaFormService {
  createNotaFormGroup(nota: NotaFormGroupInput = { id: null }): NotaFormGroup {
    const notaRawValue = {
      ...this.getFormDefaults(),
      ...nota,
    };
    return new FormGroup<NotaFormGroupContent>({
      id: new FormControl(
        { value: notaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      pontuacao: new FormControl(notaRawValue.pontuacao, {
        validators: [Validators.required],
      }),
      data: new FormControl(notaRawValue.data, {
        validators: [Validators.required],
      }),
      disciplina: new FormControl(notaRawValue.disciplina),
      aluno: new FormControl(notaRawValue.aluno),
    });
  }

  getNota(form: NotaFormGroup): INota | NewNota {
    return form.getRawValue() as INota | NewNota;
  }

  resetForm(form: NotaFormGroup, nota: NotaFormGroupInput): void {
    const notaRawValue = { ...this.getFormDefaults(), ...nota };
    form.reset(
      {
        ...notaRawValue,
        id: { value: notaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NotaFormDefaults {
    return {
      id: null,
    };
  }
}
