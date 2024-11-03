import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDisciplina } from '../disciplina.model';
import { DisciplinaService } from '../service/disciplina.service';

@Component({
  standalone: true,
  templateUrl: './disciplina-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DisciplinaDeleteDialogComponent {
  disciplina?: IDisciplina;

  protected disciplinaService = inject(DisciplinaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.disciplinaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
