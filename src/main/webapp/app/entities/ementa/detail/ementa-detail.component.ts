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

    // Divide o texto para caber em até 6 linhas dentro da largura de 180 unidades
    const descricaoText = doc.splitTextToSize(ementaValue?.descricao ?? '-', 180).slice(0, 6);
    doc.text(descricaoText, 10, y + 10);

    // Define a altura da moldura para comportar exatamente 6 linhas (aproximadamente 10 unidades por linha)
    const alturaMoldura = 20; // 6 linhas com altura de 10 unidades cada
    doc.rect(8, y + 5, 184, alturaMoldura + 10); // Moldura ao redor do texto com altura ajustada
    y += alturaMoldura + 20; // Ajusta 'y' com espaçamento adicional para a próxima seção

    // Bibliografia Básica
    doc.setFontSize(12);
    doc.text('Bibliografia Básica:', 10, y);
    doc.setFontSize(10);
    const bibliografiaBasicaText = doc.splitTextToSize(ementaValue?.bibliografiaBasica ?? '-', 180).slice(0, 10);
    doc.text(bibliografiaBasicaText, 10, y + 10);

    // Dimensões e moldura da Bibliografia Básica
    const bibliografiaBasicaHeight = 20;
    doc.rect(8, y + 5, 184, bibliografiaBasicaHeight + 25); // Moldura ao redor do texto
    y += bibliografiaBasicaHeight + 40; // Ajusta 'y' com espaçamento adicional para a próxima seção

    // Bibliografia Complementar
    doc.setFontSize(12);
    doc.text('Bibliografia Complementar:', 10, y);
    doc.setFontSize(10);
    const bibliografiaComplementarText = doc.splitTextToSize(ementaValue?.bibliografiaComplementar ?? '-', 180).slice(0, 10);
    doc.text(bibliografiaComplementarText, 10, y + 10);

    // Dimensões e moldura da Bibliografia Complementar
    const bibliografiaComplementarHeight = 23;
    doc.rect(8, y + 5, 184, bibliografiaComplementarHeight + 20); // Moldura ao redor do texto
    y += bibliografiaComplementarHeight + 30; // Ajusta 'y' para a próxima seção

    // Prática Laboratorial
    doc.setFontSize(12);
    doc.text('Prática Laboratorial:', 10, y);
    doc.setFontSize(10);
    const praticaLaboratorialText = doc.splitTextToSize(ementaValue?.praticaLaboratorial ?? '-', 180).slice(0, 7);
    doc.text(praticaLaboratorialText, 10, y + 10);

    // Dimensões e moldura da Prática Laboratorial
    const praticaLaboratorialHeight = 20;
    doc.rect(8, y + 5, 184, praticaLaboratorialHeight + 20); // Moldura ao redor do texto
    y += praticaLaboratorialHeight + 30; // Ajusta 'y' para a próxima seção

    // Professor
    doc.setFontSize(12);
    doc.text('Professor:', 10, y);
    doc.setFontSize(10);
    doc.text(ementaValue?.professor?.nome ?? '-', 32, y);
    y += 10;

    // Salvando o PDF
    const nomeDisciplina = ementaValue?.disciplina?.nome;
    doc.save(`${nomeDisciplina}.pdf`);
  }
}
