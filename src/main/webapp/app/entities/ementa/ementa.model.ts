import { IProfessor } from 'app/entities/professor/professor.model';
import { ICurso } from 'app/entities/curso/curso.model';
import { IDisciplina } from 'app/entities/disciplina/disciplina.model';

export interface IEmenta {
  id: number;
  descricao?: string | null;
  bibliografiaBasica?: string | null;
  bibliografiaComplementar?: string | null;
  praticaLaboratorial?: string | null;
  professor?: Pick<IProfessor, 'id' | 'nome'> | null;
  curso?: Pick<ICurso, 'id' | 'nome'> | null;
  disciplina?: Pick<IDisciplina, 'id' | 'nome'> | null;
}

export type NewEmenta = Omit<IEmenta, 'id'> & { id: null };
