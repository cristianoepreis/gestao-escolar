import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAdministrador } from '../administrador.model';
import { AdministradorService } from '../service/administrador.service';

@Component({
  standalone: true,
  templateUrl: './administrador-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AdministradorDeleteDialogComponent {
  administrador?: IAdministrador;

  protected administradorService = inject(AdministradorService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.administradorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
