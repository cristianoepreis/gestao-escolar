import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMatricula, NewMatricula } from '../matricula.model';

export type PartialUpdateMatricula = Partial<IMatricula> & Pick<IMatricula, 'id'>;

type RestOf<T extends IMatricula | NewMatricula> = Omit<T, 'dataDeMatricula'> & {
  dataDeMatricula?: string | null;
};

export type RestMatricula = RestOf<IMatricula>;

export type NewRestMatricula = RestOf<NewMatricula>;

export type PartialUpdateRestMatricula = RestOf<PartialUpdateMatricula>;

export type EntityResponseType = HttpResponse<IMatricula>;
export type EntityArrayResponseType = HttpResponse<IMatricula[]>;

@Injectable({ providedIn: 'root' })
export class MatriculaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/matriculas');

  create(matricula: NewMatricula): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(matricula);
    return this.http
      .post<RestMatricula>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(matricula: IMatricula): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(matricula);
    return this.http
      .put<RestMatricula>(`${this.resourceUrl}/${this.getMatriculaIdentifier(matricula)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(matricula: PartialUpdateMatricula): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(matricula);
    return this.http
      .patch<RestMatricula>(`${this.resourceUrl}/${this.getMatriculaIdentifier(matricula)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMatricula>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMatricula[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMatriculaIdentifier(matricula: Pick<IMatricula, 'id'>): number {
    return matricula.id;
  }

  compareMatricula(o1: Pick<IMatricula, 'id'> | null, o2: Pick<IMatricula, 'id'> | null): boolean {
    return o1 && o2 ? this.getMatriculaIdentifier(o1) === this.getMatriculaIdentifier(o2) : o1 === o2;
  }

  addMatriculaToCollectionIfMissing<Type extends Pick<IMatricula, 'id'>>(
    matriculaCollection: Type[],
    ...matriculasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const matriculas: Type[] = matriculasToCheck.filter(isPresent);
    if (matriculas.length > 0) {
      const matriculaCollectionIdentifiers = matriculaCollection.map(matriculaItem => this.getMatriculaIdentifier(matriculaItem));
      const matriculasToAdd = matriculas.filter(matriculaItem => {
        const matriculaIdentifier = this.getMatriculaIdentifier(matriculaItem);
        if (matriculaCollectionIdentifiers.includes(matriculaIdentifier)) {
          return false;
        }
        matriculaCollectionIdentifiers.push(matriculaIdentifier);
        return true;
      });
      return [...matriculasToAdd, ...matriculaCollection];
    }
    return matriculaCollection;
  }

  protected convertDateFromClient<T extends IMatricula | NewMatricula | PartialUpdateMatricula>(matricula: T): RestOf<T> {
    return {
      ...matricula,
      dataDeMatricula: matricula.dataDeMatricula?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMatricula: RestMatricula): IMatricula {
    return {
      ...restMatricula,
      dataDeMatricula: restMatricula.dataDeMatricula ? dayjs(restMatricula.dataDeMatricula) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMatricula>): HttpResponse<IMatricula> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMatricula[]>): HttpResponse<IMatricula[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
