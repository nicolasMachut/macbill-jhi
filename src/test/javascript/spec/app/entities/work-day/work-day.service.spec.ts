/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { WorkDayService } from 'app/entities/work-day/work-day.service';
import { IWorkDay, WorkDay } from 'app/shared/model/work-day.model';

describe('Service Tests', () => {
    describe('WorkDay Service', () => {
        let injector: TestBed;
        let service: WorkDayService;
        let httpMock: HttpTestingController;
        let elemDefault: IWorkDay;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(WorkDayService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new WorkDay(0, false, false, currentDate, currentDate, currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        end: currentDate.format(DATE_FORMAT),
                        start: currentDate.format(DATE_FORMAT),
                        title: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a WorkDay', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        end: currentDate.format(DATE_FORMAT),
                        start: currentDate.format(DATE_FORMAT),
                        title: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        end: currentDate,
                        start: currentDate,
                        title: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new WorkDay(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a WorkDay', async () => {
                const returnedFromService = Object.assign(
                    {
                        allDay: true,
                        draggable: true,
                        end: currentDate.format(DATE_FORMAT),
                        start: currentDate.format(DATE_FORMAT),
                        title: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        end: currentDate,
                        start: currentDate,
                        title: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of WorkDay', async () => {
                const returnedFromService = Object.assign(
                    {
                        allDay: true,
                        draggable: true,
                        end: currentDate.format(DATE_FORMAT),
                        start: currentDate.format(DATE_FORMAT),
                        title: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        end: currentDate,
                        start: currentDate,
                        title: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a WorkDay', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
