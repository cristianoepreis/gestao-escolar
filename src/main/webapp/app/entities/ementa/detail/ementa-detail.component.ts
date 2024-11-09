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
    doc.text('Curso:', 10, y);
    doc.text(ementaValue?.curso?.nome ?? '-', 30, y);

    doc.text('Disciplina:', 100, y);
    doc.text(ementaValue?.disciplina?.nome ?? '-', 130, y);
    y += 10;

    // Adicionando áreas de texto para Descrição e Bibliografia com moldura
    doc.setFontSize(12);

    // Descrição
    doc.text('Descrição:', 10, y);
    doc.setFontSize(10);
    const descricaoText = doc.splitTextToSize(ementaValue?.descricao ?? '-', 180);
    doc.text(descricaoText, 10, y + 10);

    // Dimensões e moldura da Descrição
    const descricaoHeight = doc.getTextDimensions(descricaoText).h;
    // doc.rect(8, y - 2, 184, descricaoHeight + 18);
    doc.rect(8, y + 5, 184, descricaoHeight + 25); // Moldura ao redor do texto
    y += descricaoHeight + 10; // Ajusta 'y' com espaçamento adicional

    // Bibliografia Básica
    doc.setFontSize(12);
    doc.text('Bibliografia Básica:', 10, y);
    doc.setFontSize(10);
    const bibliografiaBasicaText = doc.splitTextToSize(ementaValue?.bibliografiaBasica ?? '-', 180);
    doc.text(bibliografiaBasicaText, 10, y + 10);

    // Dimensões e moldura da Bibliografia Básica
    const bibliografiaBasicaHeight = doc.getTextDimensions(bibliografiaBasicaText).h;
    doc.rect(8, y - 2, 184, bibliografiaBasicaHeight + 18); // Moldura ao redor do texto
    y += bibliografiaBasicaHeight + 30;

    // Bibliografia Complementar
    doc.setFontSize(12);
    doc.text('Bibliografia Complementar:', 10, y);
    doc.setFontSize(10);
    const bibliografiaComplementarText = doc.splitTextToSize(ementaValue?.bibliografiaComplementar ?? '-', 180);
    doc.text(bibliografiaComplementarText, 10, y + 10);

    // Dimensões e moldura da Bibliografia Complementar
    const bibliografiaComplementarHeight = doc.getTextDimensions(bibliografiaComplementarText).h;
    doc.rect(8, y - 2, 184, bibliografiaComplementarHeight + 18); // Moldura ao redor do texto
    y += bibliografiaComplementarHeight + 30;

    // Prática Laboratorial
    doc.setFontSize(12);
    doc.text('Prática Laboratorial:', 10, y);
    doc.setFontSize(10);
    const praticaLaboratorialText = doc.splitTextToSize(ementaValue?.praticaLaboratorial ?? '-', 180);
    doc.text(praticaLaboratorialText, 10, y + 10);

    // Dimensões e moldura da Prática Laboratorial
    const praticaLaboratorialHeight = doc.getTextDimensions(praticaLaboratorialText).h;
    doc.rect(8, y - 2, 184, praticaLaboratorialHeight + 18); // Moldura ao redor do texto
    y += praticaLaboratorialHeight + 30;

    doc.text('Professor:', 10, y);
    doc.text(ementaValue?.professor?.nome ?? '-', 50, y);
    y += 10;

    // Salvando o PDF
    doc.save('ementa_template.pdf');
  }
}
