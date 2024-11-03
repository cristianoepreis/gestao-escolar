import { IAdministrador, NewAdministrador } from './administrador.model';

export const sampleWithRequiredData: IAdministrador = {
  id: 21451,
  nome: 'woot',
  email: 'Vitor42@bol.com.br',
};

export const sampleWithPartialData: IAdministrador = {
  id: 24833,
  nome: 'westernize recede',
  telefone: 'fly forenenst actually',
  email: 'Roberto57@bol.com.br',
};

export const sampleWithFullData: IAdministrador = {
  id: 31517,
  nome: 'uh-huh secret although',
  telefone: 'scrabble ugh',
  email: 'Mariana_Pereira@gmail.com',
};

export const sampleWithNewData: NewAdministrador = {
  nome: 'black',
  email: 'Felix84@bol.com.br',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
