import { IProfessor, NewProfessor } from './professor.model';

export const sampleWithRequiredData: IProfessor = {
  id: 31975,
  nome: 'offensively phooey',
  email: 'Marcos_Moraes12@live.com',
};

export const sampleWithPartialData: IProfessor = {
  id: 21297,
  nome: 'broadly exalted',
  telefone: 'resort schlep',
  email: 'Pablo_Pereira95@live.com',
};

export const sampleWithFullData: IProfessor = {
  id: 5413,
  nome: 'while of',
  especializacao: 'yuck whose down',
  telefone: 'and',
  email: 'Alicia_Xavier@gmail.com',
};

export const sampleWithNewData: NewProfessor = {
  nome: 'furthermore',
  email: 'Marcia_Reis67@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
