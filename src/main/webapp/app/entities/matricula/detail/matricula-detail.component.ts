import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMatricula } from '../matricula.model';

@Component({
  standalone: true,
  selector: 'jhi-matricula-detail',
  templateUrl: './matricula-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MatriculaDetailComponent {
  matricula = input<IMatricula | null>(null);

  previousState(): void {
    window.history.back();
  }
}
