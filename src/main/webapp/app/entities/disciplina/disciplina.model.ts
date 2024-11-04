import { ICurso } from 'app/entities/curso/curso.model';
import { IProfessor } from 'app/entities/professor/professor.model';

export interface IDisciplina {
  id: number;
  nome?: string | null;
  codigo?: string | null;
  cargaHoraria?: number | null;
  curso?: Pick<ICurso, 'id' | 'nome'> | null;
  professors?: Pick<IProfessor, 'id' | 'nome'>[] | null;
}

export type NewDisciplina = Omit<IDisciplina, 'id'> & { id: null };
