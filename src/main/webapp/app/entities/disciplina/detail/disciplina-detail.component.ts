import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDisciplina } from '../disciplina.model';

@Component({
  standalone: true,
  selector: 'jhi-disciplina-detail',
  templateUrl: './disciplina-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DisciplinaDetailComponent {
  disciplina = input<IDisciplina | null>(null);

  previousState(): void {
    window.history.back();
  }
}
