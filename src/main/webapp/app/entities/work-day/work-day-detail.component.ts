import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkDay } from 'app/shared/model/work-day.model';

@Component({
    selector: 'jhi-work-day-detail',
    templateUrl: './work-day-detail.component.html'
})
export class WorkDayDetailComponent implements OnInit {
    workDay: IWorkDay;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ workDay }) => {
            this.workDay = workDay;
        });
    }

    previousState() {
        window.history.back();
    }
}
