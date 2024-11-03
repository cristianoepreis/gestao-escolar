import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmenta, NewEmenta } from '../ementa.model';

export type PartialUpdateEmenta = Partial<IEmenta> & Pick<IEmenta, 'id'>;

export type EntityResponseType = HttpResponse<IEmenta>;
export type EntityArrayResponseType = HttpResponse<IEmenta[]>;

@Injectable({ providedIn: 'root' })
export class EmentaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ementas');

  create(ementa: NewEmenta): Observable<EntityResponseType> {
    return this.http.post<IEmenta>(this.resourceUrl, ementa, { observe: 'response' });
  }

  update(ementa: IEmenta): Observable<EntityResponseType> {
    return this.http.put<IEmenta>(`${this.resourceUrl}/${this.getEmentaIdentifier(ementa)}`, ementa, { observe: 'response' });
  }

  partialUpdate(ementa: PartialUpdateEmenta): Observable<EntityResponseType> {
    return this.http.patch<IEmenta>(`${this.resourceUrl}/${this.getEmentaIdentifier(ementa)}`, ementa, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmenta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmenta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmentaIdentifier(ementa: Pick<IEmenta, 'id'>): number {
    return ementa.id;
  }

  compareEmenta(o1: Pick<IEmenta, 'id'> | null, o2: Pick<IEmenta, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmentaIdentifier(o1) === this.getEmentaIdentifier(o2) : o1 === o2;
  }

  addEmentaToCollectionIfMissing<Type extends Pick<IEmenta, 'id'>>(
    ementaCollection: Type[],
    ...ementasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ementas: Type[] = ementasToCheck.filter(isPresent);
    if (ementas.length > 0) {
      const ementaCollectionIdentifiers = ementaCollection.map(ementaItem => this.getEmentaIdentifier(ementaItem));
      const ementasToAdd = ementas.filter(ementaItem => {
        const ementaIdentifier = this.getEmentaIdentifier(ementaItem);
        if (ementaCollectionIdentifiers.includes(ementaIdentifier)) {
          return false;
        }
        ementaCollectionIdentifiers.push(ementaIdentifier);
        return true;
      });
      return [...ementasToAdd, ...ementaCollection];
    }
    return ementaCollection;
  }
}
