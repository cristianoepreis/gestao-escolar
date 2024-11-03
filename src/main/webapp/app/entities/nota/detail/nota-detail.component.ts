import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { INota } from '../nota.model';

@Component({
  standalone: true,
  selector: 'jhi-nota-detail',
  templateUrl: './nota-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class NotaDetailComponent {
  nota = input<INota | null>(null);

  previousState(): void {
    window.history.back();
  }
}
