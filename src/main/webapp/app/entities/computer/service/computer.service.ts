import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComputer, NewComputer } from '../computer.model';

export type PartialUpdateComputer = Partial<IComputer> & Pick<IComputer, 'id'>;

export type EntityResponseType = HttpResponse<IComputer>;
export type EntityArrayResponseType = HttpResponse<IComputer[]>;

@Injectable({ providedIn: 'root' })
export class ComputerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/computers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(computer: NewComputer): Observable<EntityResponseType> {
    return this.http.post<IComputer>(this.resourceUrl, computer, { observe: 'response' });
  }

  update(computer: IComputer): Observable<EntityResponseType> {
    return this.http.put<IComputer>(`${this.resourceUrl}/${this.getComputerIdentifier(computer)}`, computer, { observe: 'response' });
  }

  partialUpdate(computer: PartialUpdateComputer): Observable<EntityResponseType> {
    return this.http.patch<IComputer>(`${this.resourceUrl}/${this.getComputerIdentifier(computer)}`, computer, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IComputer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IComputer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getComputerIdentifier(computer: Pick<IComputer, 'id'>): number {
    return computer.id;
  }

  compareComputer(o1: Pick<IComputer, 'id'> | null, o2: Pick<IComputer, 'id'> | null): boolean {
    return o1 && o2 ? this.getComputerIdentifier(o1) === this.getComputerIdentifier(o2) : o1 === o2;
  }

  addComputerToCollectionIfMissing<Type extends Pick<IComputer, 'id'>>(
    computerCollection: Type[],
    ...computersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const computers: Type[] = computersToCheck.filter(isPresent);
    if (computers.length > 0) {
      const computerCollectionIdentifiers = computerCollection.map(computerItem => this.getComputerIdentifier(computerItem)!);
      const computersToAdd = computers.filter(computerItem => {
        const computerIdentifier = this.getComputerIdentifier(computerItem);
        if (computerCollectionIdentifiers.includes(computerIdentifier)) {
          return false;
        }
        computerCollectionIdentifiers.push(computerIdentifier);
        return true;
      });
      return [...computersToAdd, ...computerCollection];
    }
    return computerCollection;
  }
}
