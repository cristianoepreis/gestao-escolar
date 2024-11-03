import dayjs from 'dayjs/esm';
import { IAluno } from 'app/entities/aluno/aluno.model';
import { ICurso } from 'app/entities/curso/curso.model';
import { StatusMatricula } from 'app/entities/enumerations/status-matricula.model';

export interface IMatricula {
  id: number;
  dataDeMatricula?: dayjs.Dayjs | null;
  status?: keyof typeof StatusMatricula | null;
  aluno?: Pick<IAluno, 'id' | 'nome'> | null;
  curso?: Pick<ICurso, 'id' | 'nome'> | null;
}

export type NewMatricula = Omit<IMatricula, 'id'> & { id: null };
