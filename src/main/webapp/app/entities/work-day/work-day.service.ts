import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IWorkDay } from 'app/shared/model/work-day.model';

type EntityResponseType = HttpResponse<IWorkDay>;
type EntityArrayResponseType = HttpResponse<IWorkDay[]>;

@Injectable({ providedIn: 'root' })
export class WorkDayService {
    public resourceUrl = SERVER_API_URL + 'api/work-days';

    constructor(protected http: HttpClient) {}

    create(workDay: IWorkDay): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(workDay);
        return this.http
            .post<IWorkDay>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(workDay: IWorkDay): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(workDay);
        return this.http
            .put<IWorkDay>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IWorkDay>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IWorkDay[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(workDay: IWorkDay): IWorkDay {
        const copy: IWorkDay = Object.assign({}, workDay, {
            end: workDay.end != null && workDay.end.isValid() ? workDay.end.format(DATE_FORMAT) : null,
            start: workDay.start != null && workDay.start.isValid() ? workDay.start.format(DATE_FORMAT) : null,
            title: workDay.title != null && workDay.title.isValid() ? workDay.title.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.end = res.body.end != null ? moment(res.body.end) : null;
            res.body.start = res.body.start != null ? moment(res.body.start) : null;
            res.body.title = res.body.title != null ? moment(res.body.title) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((workDay: IWorkDay) => {
                workDay.end = workDay.end != null ? moment(workDay.end) : null;
                workDay.start = workDay.start != null ? moment(workDay.start) : null;
                workDay.title = workDay.title != null ? moment(workDay.title) : null;
            });
        }
        return res;
    }
}
