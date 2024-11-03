import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProfessor, NewProfessor } from '../professor.model';

export type PartialUpdateProfessor = Partial<IProfessor> & Pick<IProfessor, 'id'>;

export type EntityResponseType = HttpResponse<IProfessor>;
export type EntityArrayResponseType = HttpResponse<IProfessor[]>;

@Injectable({ providedIn: 'root' })
export class ProfessorService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/professors');

  create(professor: NewProfessor): Observable<EntityResponseType> {
    return this.http.post<IProfessor>(this.resourceUrl, professor, { observe: 'response' });
  }

  update(professor: IProfessor): Observable<EntityResponseType> {
    return this.http.put<IProfessor>(`${this.resourceUrl}/${this.getProfessorIdentifier(professor)}`, professor, { observe: 'response' });
  }

  partialUpdate(professor: PartialUpdateProfessor): Observable<EntityResponseType> {
    return this.http.patch<IProfessor>(`${this.resourceUrl}/${this.getProfessorIdentifier(professor)}`, professor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProfessor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfessor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProfessorIdentifier(professor: Pick<IProfessor, 'id'>): number {
    return professor.id;
  }

  compareProfessor(o1: Pick<IProfessor, 'id'> | null, o2: Pick<IProfessor, 'id'> | null): boolean {
    return o1 && o2 ? this.getProfessorIdentifier(o1) === this.getProfessorIdentifier(o2) : o1 === o2;
  }

  addProfessorToCollectionIfMissing<Type extends Pick<IProfessor, 'id'>>(
    professorCollection: Type[],
    ...professorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const professors: Type[] = professorsToCheck.filter(isPresent);
    if (professors.length > 0) {
      const professorCollectionIdentifiers = professorCollection.map(professorItem => this.getProfessorIdentifier(professorItem));
      const professorsToAdd = professors.filter(professorItem => {
        const professorIdentifier = this.getProfessorIdentifier(professorItem);
        if (professorCollectionIdentifiers.includes(professorIdentifier)) {
          return false;
        }
        professorCollectionIdentifiers.push(professorIdentifier);
        return true;
      });
      return [...professorsToAdd, ...professorCollection];
    }
    return professorCollection;
  }
}
