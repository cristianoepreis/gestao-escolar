import dayjs from 'dayjs/esm';

import { IAluno, NewAluno } from './aluno.model';

export const sampleWithRequiredData: IAluno = {
  id: 5994,
  nome: 'while consequently than',
  dataDeNascimento: dayjs('2024-11-03'),
  cpf: 'marketplace',
  email: 'EnzoGabriel_Melo2@live.com',
};

export const sampleWithPartialData: IAluno = {
  id: 16852,
  nome: 'while',
  dataDeNascimento: dayjs('2024-11-03'),
  cpf: 'meh override',
  email: 'Danilo65@gmail.com',
};

export const sampleWithFullData: IAluno = {
  id: 27532,
  nome: 'orderly',
  dataDeNascimento: dayjs('2024-11-03'),
  cpf: 'however openly',
  endereco: 'brightly upright',
  telefone: 'only',
  email: 'Pietro_Santos67@gmail.com',
};

export const sampleWithNewData: NewAluno = {
  nome: 'what',
  dataDeNascimento: dayjs('2024-11-03'),
  cpf: 'rosin',
  email: 'Yuri.Saraiva@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
