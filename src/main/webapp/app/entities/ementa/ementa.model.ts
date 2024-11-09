import { IProfessor } from 'app/entities/professor/professor.model';
import { ICurso } from 'app/entities/curso/curso.model';
import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { Instance } from '@popperjs/core';
import dayjs from 'dayjs';

export interface IEmenta {
  id: number;
  descricao?: string | null;
  bibliografiaBasica?: string | null;
  bibliografiaComplementar?: string | null;
  praticaLaboratorial?: string | null;
  ultimaAlteracao?: dayjs.Dayjs | null;
  professor?: Pick<IProfessor, 'id' | 'nome'> | null;
  curso?: Pick<ICurso, 'id' | 'nome'> | null;
  disciplina?: Pick<IDisciplina, 'id' | 'nome'> | null;
}

export type NewEmenta = Omit<IEmenta, 'id'> & { id: null };
