import { IDisciplina, NewDisciplina } from './disciplina.model';

export const sampleWithRequiredData: IDisciplina = {
  id: 8604,
  nome: 'unlike substantiate shabby',
  codigo: 'capitalize freely an',
  cargaHoraria: 26256,
};

export const sampleWithPartialData: IDisciplina = {
  id: 4374,
  nome: 'alongside',
  codigo: 'why',
  cargaHoraria: 23164,
};

export const sampleWithFullData: IDisciplina = {
  id: 20790,
  nome: 'unfortunately',
  codigo: 'char remorseful',
  cargaHoraria: 12938,
};

export const sampleWithNewData: NewDisciplina = {
  nome: 'waterspout boggle pinstripe',
  codigo: 'blah considering however',
  cargaHoraria: 9950,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
