import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INota, NewNota } from '../nota.model';

export type PartialUpdateNota = Partial<INota> & Pick<INota, 'id'>;

type RestOf<T extends INota | NewNota> = Omit<T, 'data'> & {
  data?: string | null;
};

export type RestNota = RestOf<INota>;

export type NewRestNota = RestOf<NewNota>;

export type PartialUpdateRestNota = RestOf<PartialUpdateNota>;

export type EntityResponseType = HttpResponse<INota>;
export type EntityArrayResponseType = HttpResponse<INota[]>;

@Injectable({ providedIn: 'root' })
export class NotaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notas');

  create(nota: NewNota): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nota);
    return this.http.post<RestNota>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(nota: INota): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nota);
    return this.http
      .put<RestNota>(`${this.resourceUrl}/${this.getNotaIdentifier(nota)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(nota: PartialUpdateNota): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nota);
    return this.http
      .patch<RestNota>(`${this.resourceUrl}/${this.getNotaIdentifier(nota)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNota>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNota[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNotaIdentifier(nota: Pick<INota, 'id'>): number {
    return nota.id;
  }

  compareNota(o1: Pick<INota, 'id'> | null, o2: Pick<INota, 'id'> | null): boolean {
    return o1 && o2 ? this.getNotaIdentifier(o1) === this.getNotaIdentifier(o2) : o1 === o2;
  }

  addNotaToCollectionIfMissing<Type extends Pick<INota, 'id'>>(
    notaCollection: Type[],
    ...notasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const notas: Type[] = notasToCheck.filter(isPresent);
    if (notas.length > 0) {
      const notaCollectionIdentifiers = notaCollection.map(notaItem => this.getNotaIdentifier(notaItem));
      const notasToAdd = notas.filter(notaItem => {
        const notaIdentifier = this.getNotaIdentifier(notaItem);
        if (notaCollectionIdentifiers.includes(notaIdentifier)) {
          return false;
        }
        notaCollectionIdentifiers.push(notaIdentifier);
        return true;
      });
      return [...notasToAdd, ...notaCollection];
    }
    return notaCollection;
  }

  protected convertDateFromClient<T extends INota | NewNota | PartialUpdateNota>(nota: T): RestOf<T> {
    return {
      ...nota,
      data: nota.data?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restNota: RestNota): INota {
    return {
      ...restNota,
      data: restNota.data ? dayjs(restNota.data) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestNota>): HttpResponse<INota> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestNota[]>): HttpResponse<INota[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
