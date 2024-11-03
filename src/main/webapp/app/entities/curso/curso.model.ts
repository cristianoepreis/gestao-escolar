import { IAluno } from 'app/entities/aluno/aluno.model';

export interface ICurso {
  id: number;
  nome?: string | null;
  descricao?: string | null;
  duracao?: number | null;
  alunos?: Pick<IAluno, 'id'>[] | null;
}

export type NewCurso = Omit<ICurso, 'id'> & { id: null };
