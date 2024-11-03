import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICurso } from '../curso.model';

@Component({
  standalone: true,
  selector: 'jhi-curso-detail',
  templateUrl: './curso-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CursoDetailComponent {
  curso = input<ICurso | null>(null);

  previousState(): void {
    window.history.back();
  }
}
