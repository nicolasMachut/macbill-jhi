import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IWorkDay } from 'app/shared/model/work-day.model';
import { WorkDayService } from './work-day.service';
import { IUser, UserService } from 'app/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
    selector: 'jhi-work-day-update',
    templateUrl: './work-day-update.component.html'
})
export class WorkDayUpdateComponent implements OnInit {
    workDay: IWorkDay;
    isSaving: boolean;

    users: IUser[];

    customers: ICustomer[];
    endDp: any;
    startDp: any;
    titleDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected workDayService: WorkDayService,
        protected userService: UserService,
        protected customerService: CustomerService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ workDay }) => {
            this.workDay = workDay;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.customerService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICustomer[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICustomer[]>) => response.body)
            )
            .subscribe((res: ICustomer[]) => (this.customers = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.workDay.id !== undefined) {
            this.subscribeToSaveResponse(this.workDayService.update(this.workDay));
        } else {
            this.subscribeToSaveResponse(this.workDayService.create(this.workDay));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkDay>>) {
        result.subscribe((res: HttpResponse<IWorkDay>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackCustomerById(index: number, item: ICustomer) {
        return item.id;
    }
}
