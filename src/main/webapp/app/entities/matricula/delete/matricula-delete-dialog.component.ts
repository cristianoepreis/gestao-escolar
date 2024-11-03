import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMatricula } from '../matricula.model';
import { MatriculaService } from '../service/matricula.service';

@Component({
  standalone: true,
  templateUrl: './matricula-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MatriculaDeleteDialogComponent {
  matricula?: IMatricula;

  protected matriculaService = inject(MatriculaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.matriculaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
