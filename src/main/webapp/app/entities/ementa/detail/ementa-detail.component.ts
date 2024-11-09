import { Component, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IEmenta } from '../ementa.model';
import { jsPDF } from 'jspdf';

@Component({
  standalone: true,
  selector: 'jhi-ementa-detail',
  templateUrl: './ementa-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class EmentaDetailComponent {
  ementa = input<IEmenta | null>(null);

  protected dataUtils = inject(DataUtils);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  // generatePDF(): void {
  //   const doc = new jsPDF();

  //   // Seleciona o conteúdo HTML e força o tipo para HTMLElement
  //   const content = document.querySelector('.d-flex') as HTMLElement;
  //   // Remover a verificação condicional desnecessária, pois `content` sempre será truthy
  //   doc.html(content, {
  //     callback(pdfDoc) {
  //       pdfDoc.save('ementa.pdf'); // Salva o PDF com o nome "ementa.pdf"
  //     },
  //     x: 10,
  //     y: 10,
  //     width: 180, // Define a largura do conteúdo no PDF
  //     windowWidth: 675, // Largura da janela de visualização
  //   });
  // }

  generatePDF(): void {
    const doc = new jsPDF();

    // Definindo cabeçalho
    doc.setFontSize(16);
    doc.text('Detalhes da Ementa', 105, 20, { align: 'center' }); // Cabeçalho centralizado
    doc.setFontSize(12);
    doc.text('Instituição XYZ', 105, 28, { align: 'center' }); // Subcabeçalho
    doc.line(10, 32, 200, 32); // Linha horizontal abaixo do cabeçalho

    // Espaçamento inicial
    let y = 40;
    const ementaValue = this.ementa(); // Acessando o valor do sinal

    // Posições dos textos
    doc.text('Disciplina:', 10, y);
    doc.text(ementaValue?.disciplina?.nome ?? '-', 50, y);
    y += 10;

    doc.text('Professor:', 10, y);
    doc.text(ementaValue?.professor?.nome ?? '-', 50, y);
    y += 10;

    doc.text('Curso:', 10, y);
    doc.text(ementaValue?.curso?.nome ?? '-', 50, y);
    y += 10;

    // Adicionando áreas de texto para Descrição e Bibliografia
    doc.setFontSize(12);
    doc.text('Descrição:', 10, y);
    doc.setFontSize(10);
    doc.text(ementaValue?.descricao ?? '-', 10, y + 10, { maxWidth: 180 }); // Quebra de linha automática para texto longo
    y += 30;

    doc.setFontSize(12);
    doc.text('Bibliografia Básica:', 10, y);
    doc.setFontSize(10);
    doc.text(ementaValue?.bibliografiaBasica ?? '-', 10, y + 10, { maxWidth: 180 });
    y += 30;

    doc.setFontSize(12);
    doc.text('Bibliografia Complementar:', 10, y);
    doc.setFontSize(10);
    doc.text(ementaValue?.bibliografiaComplementar ?? '-', 10, y + 10, { maxWidth: 180 });
    y += 30;

    doc.setFontSize(12);
    doc.text('Prática Laboratorial:', 10, y);
    doc.setFontSize(10);
    doc.splitTextToSize(ementaValue?.praticaLaboratorial ?? '-', 10, y + 10);

    // Salvando o PDF
    doc.save('ementa_template.pdf');
  }
}
