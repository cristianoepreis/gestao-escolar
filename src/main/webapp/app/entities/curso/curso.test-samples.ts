import { ICurso, NewCurso } from './curso.model';

export const sampleWithRequiredData: ICurso = {
  id: 10088,
  nome: 'blindly',
  duracao: 20030,
};

export const sampleWithPartialData: ICurso = {
  id: 13580,
  nome: 'partridge smarten yowza',
  descricao: 'any ew during',
  duracao: 3846,
};

export const sampleWithFullData: ICurso = {
  id: 11497,
  nome: 'chunder vivacious oh',
  descricao: 'judgementally till',
  duracao: 7287,
};

export const sampleWithNewData: NewCurso = {
  nome: 'selfishly',
  duracao: 22761,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
