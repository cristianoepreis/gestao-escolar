import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '2f0b6535-722e-4b13-94bf-8b6c9b43810e',
};

export const sampleWithPartialData: IAuthority = {
  name: '9e29bb67-8a9a-47b5-9c2b-21b89f82368a',
};

export const sampleWithFullData: IAuthority = {
  name: 'e1141ffe-0e87-486a-b4cb-8e7a09142795',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
