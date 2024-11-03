import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdministrador, NewAdministrador } from '../administrador.model';

export type PartialUpdateAdministrador = Partial<IAdministrador> & Pick<IAdministrador, 'id'>;

export type EntityResponseType = HttpResponse<IAdministrador>;
export type EntityArrayResponseType = HttpResponse<IAdministrador[]>;

@Injectable({ providedIn: 'root' })
export class AdministradorService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/administradors');

  create(administrador: NewAdministrador): Observable<EntityResponseType> {
    return this.http.post<IAdministrador>(this.resourceUrl, administrador, { observe: 'response' });
  }

  update(administrador: IAdministrador): Observable<EntityResponseType> {
    return this.http.put<IAdministrador>(`${this.resourceUrl}/${this.getAdministradorIdentifier(administrador)}`, administrador, {
      observe: 'response',
    });
  }

  partialUpdate(administrador: PartialUpdateAdministrador): Observable<EntityResponseType> {
    return this.http.patch<IAdministrador>(`${this.resourceUrl}/${this.getAdministradorIdentifier(administrador)}`, administrador, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdministrador>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdministrador[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAdministradorIdentifier(administrador: Pick<IAdministrador, 'id'>): number {
    return administrador.id;
  }

  compareAdministrador(o1: Pick<IAdministrador, 'id'> | null, o2: Pick<IAdministrador, 'id'> | null): boolean {
    return o1 && o2 ? this.getAdministradorIdentifier(o1) === this.getAdministradorIdentifier(o2) : o1 === o2;
  }

  addAdministradorToCollectionIfMissing<Type extends Pick<IAdministrador, 'id'>>(
    administradorCollection: Type[],
    ...administradorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const administradors: Type[] = administradorsToCheck.filter(isPresent);
    if (administradors.length > 0) {
      const administradorCollectionIdentifiers = administradorCollection.map(administradorItem =>
        this.getAdministradorIdentifier(administradorItem),
      );
      const administradorsToAdd = administradors.filter(administradorItem => {
        const administradorIdentifier = this.getAdministradorIdentifier(administradorItem);
        if (administradorCollectionIdentifiers.includes(administradorIdentifier)) {
          return false;
        }
        administradorCollectionIdentifiers.push(administradorIdentifier);
        return true;
      });
      return [...administradorsToAdd, ...administradorCollection];
    }
    return administradorCollection;
  }
}
