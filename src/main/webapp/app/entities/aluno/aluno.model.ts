import dayjs from 'dayjs/esm';
import { ICurso } from 'app/entities/curso/curso.model';

export interface IAluno {
  id: number;
  nome?: string | null;
  dataDeNascimento?: dayjs.Dayjs | null;
  cpf?: string | null;
  endereco?: string | null;
  telefone?: string | null;
  email?: string | null;
  cursos?: Pick<ICurso, 'id' | 'nome'>[] | null;
}

export type NewAluno = Omit<IAluno, 'id'> & { id: null };
