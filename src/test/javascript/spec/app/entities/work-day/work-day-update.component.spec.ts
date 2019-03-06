/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MacbillTestModule } from '../../../test.module';
import { WorkDayUpdateComponent } from 'app/entities/work-day/work-day-update.component';
import { WorkDayService } from 'app/entities/work-day/work-day.service';
import { WorkDay } from 'app/shared/model/work-day.model';

describe('Component Tests', () => {
    describe('WorkDay Management Update Component', () => {
        let comp: WorkDayUpdateComponent;
        let fixture: ComponentFixture<WorkDayUpdateComponent>;
        let service: WorkDayService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MacbillTestModule],
                declarations: [WorkDayUpdateComponent]
            })
                .overrideTemplate(WorkDayUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WorkDayUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkDayService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new WorkDay(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.workDay = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new WorkDay();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.workDay = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
