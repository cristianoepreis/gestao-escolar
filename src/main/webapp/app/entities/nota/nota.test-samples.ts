import dayjs from 'dayjs/esm';

import { INota, NewNota } from './nota.model';

export const sampleWithRequiredData: INota = {
  id: 5295,
  pontuacao: 6592.07,
  data: dayjs('2024-11-03'),
};

export const sampleWithPartialData: INota = {
  id: 2701,
  pontuacao: 22428.58,
  data: dayjs('2024-11-02'),
};

export const sampleWithFullData: INota = {
  id: 7063,
  pontuacao: 1824.83,
  data: dayjs('2024-11-02'),
};

export const sampleWithNewData: NewNota = {
  pontuacao: 28395.69,
  data: dayjs('2024-11-03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
