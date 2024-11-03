import dayjs from 'dayjs/esm';
import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { IAluno } from 'app/entities/aluno/aluno.model';

export interface INota {
  id: number;
  pontuacao?: number | null;
  data?: dayjs.Dayjs | null;
  disciplina?: Pick<IDisciplina, 'id' | 'nome'> | null;
  aluno?: Pick<IAluno, 'id' | 'nome'> | null;
}

export type NewNota = Omit<INota, 'id'> & { id: null };
