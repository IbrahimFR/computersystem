import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IComputer } from '../computer.model';
import { ComputerService } from '../service/computer.service';

@Injectable({ providedIn: 'root' })
export class ComputerRoutingResolveService implements Resolve<IComputer | null> {
  constructor(protected service: ComputerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IComputer | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((computer: HttpResponse<IComputer>) => {
          if (computer.body) {
            return of(computer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
