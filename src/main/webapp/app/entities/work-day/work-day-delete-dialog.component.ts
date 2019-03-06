import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWorkDay } from 'app/shared/model/work-day.model';
import { WorkDayService } from './work-day.service';

@Component({
    selector: 'jhi-work-day-delete-dialog',
    templateUrl: './work-day-delete-dialog.component.html'
})
export class WorkDayDeleteDialogComponent {
    workDay: IWorkDay;

    constructor(protected workDayService: WorkDayService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.workDayService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'workDayListModification',
                content: 'Deleted an workDay'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-work-day-delete-popup',
    template: ''
})
export class WorkDayDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ workDay }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(WorkDayDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.workDay = workDay;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/work-day', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/work-day', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
