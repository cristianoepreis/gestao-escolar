import dayjs from 'dayjs/esm';

import { IMatricula, NewMatricula } from './matricula.model';

export const sampleWithRequiredData: IMatricula = {
  id: 13530,
  dataDeMatricula: dayjs('2024-11-03'),
  status: 'ATIVO',
};

export const sampleWithPartialData: IMatricula = {
  id: 12557,
  dataDeMatricula: dayjs('2024-11-03'),
  status: 'TRANCADO',
};

export const sampleWithFullData: IMatricula = {
  id: 9719,
  dataDeMatricula: dayjs('2024-11-03'),
  status: 'TRANCADO',
};

export const sampleWithNewData: NewMatricula = {
  dataDeMatricula: dayjs('2024-11-03'),
  status: 'INATIVO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
