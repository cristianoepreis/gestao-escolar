import { IEmenta, NewEmenta } from './ementa.model';

export const sampleWithRequiredData: IEmenta = {
  id: 19129,
};

export const sampleWithPartialData: IEmenta = {
  id: 18592,
  descricao: '../fake-data/blob/hipster.txt',
  bibliografiaComplementar: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IEmenta = {
  id: 28944,
  descricao: '../fake-data/blob/hipster.txt',
  bibliografiaBasica: '../fake-data/blob/hipster.txt',
  bibliografiaComplementar: '../fake-data/blob/hipster.txt',
  praticaLaboratorial: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewEmenta = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
