import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MacbillSharedModule } from 'app/shared';
import {
    WorkDayComponent,
    WorkDayDetailComponent,
    WorkDayUpdateComponent,
    WorkDayDeletePopupComponent,
    WorkDayDeleteDialogComponent,
    workDayRoute,
    workDayPopupRoute
} from './';

const ENTITY_STATES = [...workDayRoute, ...workDayPopupRoute];

@NgModule({
    imports: [MacbillSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WorkDayComponent,
        WorkDayDetailComponent,
        WorkDayUpdateComponent,
        WorkDayDeleteDialogComponent,
        WorkDayDeletePopupComponent
    ],
    entryComponents: [WorkDayComponent, WorkDayUpdateComponent, WorkDayDeleteDialogComponent, WorkDayDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MacbillWorkDayModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
