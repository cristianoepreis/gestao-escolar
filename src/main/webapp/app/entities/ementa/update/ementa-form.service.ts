import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEmenta, NewEmenta } from '../ementa.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmenta for edit and NewEmentaFormGroupInput for create.
 */
type EmentaFormGroupInput = IEmenta | PartialWithRequiredKeyOf<NewEmenta>;

type EmentaFormDefaults = Pick<NewEmenta, 'id'>;

type EmentaFormGroupContent = {
  id: FormControl<IEmenta['id'] | NewEmenta['id']>;
  descricao: FormControl<IEmenta['descricao']>;
  bibliografiaBasica: FormControl<IEmenta['bibliografiaBasica']>;
  bibliografiaComplementar: FormControl<IEmenta['bibliografiaComplementar']>;
  praticaLaboratorial: FormControl<IEmenta['praticaLaboratorial']>;
  professor: FormControl<IEmenta['professor']>;
  curso: FormControl<IEmenta['curso']>;
  disciplina: FormControl<IEmenta['disciplina']>;
};

export type EmentaFormGroup = FormGroup<EmentaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmentaFormService {
  createEmentaFormGroup(ementa: EmentaFormGroupInput = { id: null }): EmentaFormGroup {
    const ementaRawValue = {
      ...this.getFormDefaults(),
      ...ementa,
    };
    return new FormGroup<EmentaFormGroupContent>({
      id: new FormControl(
        { value: ementaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      descricao: new FormControl(ementaRawValue.descricao),
      bibliografiaBasica: new FormControl(ementaRawValue.bibliografiaBasica),
      bibliografiaComplementar: new FormControl(ementaRawValue.bibliografiaComplementar),
      praticaLaboratorial: new FormControl(ementaRawValue.praticaLaboratorial),
      professor: new FormControl(ementaRawValue.professor),
      curso: new FormControl(ementaRawValue.curso),
      disciplina: new FormControl(ementaRawValue.disciplina),
    });
  }

  getEmenta(form: EmentaFormGroup): IEmenta | NewEmenta {
    return form.getRawValue() as IEmenta | NewEmenta;
  }

  resetForm(form: EmentaFormGroup, ementa: EmentaFormGroupInput): void {
    const ementaRawValue = { ...this.getFormDefaults(), ...ementa };
    form.reset(
      {
        ...ementaRawValue,
        id: { value: ementaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmentaFormDefaults {
    return {
      id: null,
    };
  }
}
