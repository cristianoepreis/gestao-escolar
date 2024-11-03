import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAluno } from '../aluno.model';

@Component({
  standalone: true,
  selector: 'jhi-aluno-detail',
  templateUrl: './aluno-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AlunoDetailComponent {
  aluno = input<IAluno | null>(null);

  previousState(): void {
    window.history.back();
  }
}
