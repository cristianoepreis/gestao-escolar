import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 6968,
  login: 'aS',
};

export const sampleWithPartialData: IUser = {
  id: 8651,
  login: '8@1RPYD\\-Qv\\lOWDCPB\\.LSJRk',
};

export const sampleWithFullData: IUser = {
  id: 14166,
  login: '020@nS8\\10FLc\\+h4v',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
