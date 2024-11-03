import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAdministrador } from '../administrador.model';

@Component({
  standalone: true,
  selector: 'jhi-administrador-detail',
  templateUrl: './administrador-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AdministradorDetailComponent {
  administrador = input<IAdministrador | null>(null);

  previousState(): void {
    window.history.back();
  }
}
