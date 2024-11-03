import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDisciplina, NewDisciplina } from '../disciplina.model';

export type PartialUpdateDisciplina = Partial<IDisciplina> & Pick<IDisciplina, 'id'>;

export type EntityResponseType = HttpResponse<IDisciplina>;
export type EntityArrayResponseType = HttpResponse<IDisciplina[]>;

@Injectable({ providedIn: 'root' })
export class DisciplinaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/disciplinas');

  create(disciplina: NewDisciplina): Observable<EntityResponseType> {
    return this.http.post<IDisciplina>(this.resourceUrl, disciplina, { observe: 'response' });
  }

  update(disciplina: IDisciplina): Observable<EntityResponseType> {
    return this.http.put<IDisciplina>(`${this.resourceUrl}/${this.getDisciplinaIdentifier(disciplina)}`, disciplina, {
      observe: 'response',
    });
  }

  partialUpdate(disciplina: PartialUpdateDisciplina): Observable<EntityResponseType> {
    return this.http.patch<IDisciplina>(`${this.resourceUrl}/${this.getDisciplinaIdentifier(disciplina)}`, disciplina, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDisciplina>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDisciplina[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDisciplinaIdentifier(disciplina: Pick<IDisciplina, 'id'>): number {
    return disciplina.id;
  }

  compareDisciplina(o1: Pick<IDisciplina, 'id'> | null, o2: Pick<IDisciplina, 'id'> | null): boolean {
    return o1 && o2 ? this.getDisciplinaIdentifier(o1) === this.getDisciplinaIdentifier(o2) : o1 === o2;
  }

  addDisciplinaToCollectionIfMissing<Type extends Pick<IDisciplina, 'id'>>(
    disciplinaCollection: Type[],
    ...disciplinasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const disciplinas: Type[] = disciplinasToCheck.filter(isPresent);
    if (disciplinas.length > 0) {
      const disciplinaCollectionIdentifiers = disciplinaCollection.map(disciplinaItem => this.getDisciplinaIdentifier(disciplinaItem));
      const disciplinasToAdd = disciplinas.filter(disciplinaItem => {
        const disciplinaIdentifier = this.getDisciplinaIdentifier(disciplinaItem);
        if (disciplinaCollectionIdentifiers.includes(disciplinaIdentifier)) {
          return false;
        }
        disciplinaCollectionIdentifiers.push(disciplinaIdentifier);
        return true;
      });
      return [...disciplinasToAdd, ...disciplinaCollection];
    }
    return disciplinaCollection;
  }
}
