import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { WorkDay } from 'app/shared/model/work-day.model';
import { WorkDayService } from './work-day.service';
import { WorkDayComponent } from './work-day.component';
import { WorkDayDetailComponent } from './work-day-detail.component';
import { WorkDayUpdateComponent } from './work-day-update.component';
import { WorkDayDeletePopupComponent } from './work-day-delete-dialog.component';
import { IWorkDay } from 'app/shared/model/work-day.model';

@Injectable({ providedIn: 'root' })
export class WorkDayResolve implements Resolve<IWorkDay> {
    constructor(private service: WorkDayService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IWorkDay> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<WorkDay>) => response.ok),
                map((workDay: HttpResponse<WorkDay>) => workDay.body)
            );
        }
        return of(new WorkDay());
    }
}

export const workDayRoute: Routes = [
    {
        path: '',
        component: WorkDayComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'macbillApp.workDay.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: WorkDayDetailComponent,
        resolve: {
            workDay: WorkDayResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'macbillApp.workDay.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: WorkDayUpdateComponent,
        resolve: {
            workDay: WorkDayResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'macbillApp.workDay.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: WorkDayUpdateComponent,
        resolve: {
            workDay: WorkDayResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'macbillApp.workDay.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const workDayPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: WorkDayDeletePopupComponent,
        resolve: {
            workDay: WorkDayResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'macbillApp.workDay.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
