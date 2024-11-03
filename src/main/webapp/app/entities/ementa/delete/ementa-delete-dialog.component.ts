import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmenta } from '../ementa.model';
import { EmentaService } from '../service/ementa.service';

@Component({
  standalone: true,
  templateUrl: './ementa-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmentaDeleteDialogComponent {
  ementa?: IEmenta;

  protected ementaService = inject(EmentaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ementaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
