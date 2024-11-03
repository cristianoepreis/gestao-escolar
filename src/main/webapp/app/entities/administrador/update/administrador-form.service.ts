import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAdministrador, NewAdministrador } from '../administrador.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAdministrador for edit and NewAdministradorFormGroupInput for create.
 */
type AdministradorFormGroupInput = IAdministrador | PartialWithRequiredKeyOf<NewAdministrador>;

type AdministradorFormDefaults = Pick<NewAdministrador, 'id'>;

type AdministradorFormGroupContent = {
  id: FormControl<IAdministrador['id'] | NewAdministrador['id']>;
  nome: FormControl<IAdministrador['nome']>;
  telefone: FormControl<IAdministrador['telefone']>;
  email: FormControl<IAdministrador['email']>;
};

export type AdministradorFormGroup = FormGroup<AdministradorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AdministradorFormService {
  createAdministradorFormGroup(administrador: AdministradorFormGroupInput = { id: null }): AdministradorFormGroup {
    const administradorRawValue = {
      ...this.getFormDefaults(),
      ...administrador,
    };
    return new FormGroup<AdministradorFormGroupContent>({
      id: new FormControl(
        { value: administradorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(administradorRawValue.nome, {
        validators: [Validators.required],
      }),
      telefone: new FormControl(administradorRawValue.telefone),
      email: new FormControl(administradorRawValue.email, {
        validators: [Validators.required],
      }),
    });
  }

  getAdministrador(form: AdministradorFormGroup): IAdministrador | NewAdministrador {
    return form.getRawValue() as IAdministrador | NewAdministrador;
  }

  resetForm(form: AdministradorFormGroup, administrador: AdministradorFormGroupInput): void {
    const administradorRawValue = { ...this.getFormDefaults(), ...administrador };
    form.reset(
      {
        ...administradorRawValue,
        id: { value: administradorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AdministradorFormDefaults {
    return {
      id: null,
    };
  }
}
