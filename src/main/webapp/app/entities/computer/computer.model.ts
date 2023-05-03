export interface IComputer {
  id: number;
  macAddress?: string | null;
  ipAddress?: string | null;
  employeeAbbreviation?: string | null;
  description?: string | null;
}

export type NewComputer = Omit<IComputer, 'id'> & { id: null };
