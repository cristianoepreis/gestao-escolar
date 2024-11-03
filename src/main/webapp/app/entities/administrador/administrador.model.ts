export interface IAdministrador {
  id: number;
  nome?: string | null;
  telefone?: string | null;
  email?: string | null;
}

export type NewAdministrador = Omit<IAdministrador, 'id'> & { id: null };
