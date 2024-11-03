import { IDisciplina } from 'app/entities/disciplina/disciplina.model';

export interface IProfessor {
  id: number;
  nome?: string | null;
  especializacao?: string | null;
  telefone?: string | null;
  email?: string | null;
  disciplinas?: Pick<IDisciplina, 'id' | 'nome'>[] | null;
}

export type NewProfessor = Omit<IProfessor, 'id'> & { id: null };
