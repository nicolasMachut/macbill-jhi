/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MacbillTestModule } from '../../../test.module';
import { WorkDayDetailComponent } from 'app/entities/work-day/work-day-detail.component';
import { WorkDay } from 'app/shared/model/work-day.model';

describe('Component Tests', () => {
    describe('WorkDay Management Detail Component', () => {
        let comp: WorkDayDetailComponent;
        let fixture: ComponentFixture<WorkDayDetailComponent>;
        const route = ({ data: of({ workDay: new WorkDay(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MacbillTestModule],
                declarations: [WorkDayDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(WorkDayDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WorkDayDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.workDay).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
