import { IComputer, NewComputer } from './computer.model';

export const sampleWithRequiredData: IComputer = {
  id: 54835,
};

export const sampleWithPartialData: IComputer = {
  id: 64315,
  ipAddress: 'copy',
  employeeAbbreviation: 'Massachusetts Lights',
};

export const sampleWithFullData: IComputer = {
  id: 43483,
  macAddress: 'Fish state',
  ipAddress: 'Movies Bahamas',
  employeeAbbreviation: 'Mississippi Peso',
  description: 'Rubber',
};

export const sampleWithNewData: NewComputer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
