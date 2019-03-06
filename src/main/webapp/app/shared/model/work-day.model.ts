import { Moment } from 'moment';

export interface IWorkDay {
    id?: number;
    allDay?: boolean;
    draggable?: boolean;
    end?: Moment;
    start?: Moment;
    title?: Moment;
    userLogin?: string;
    userId?: number;
    customerId?: number;
}

export class WorkDay implements IWorkDay {
    constructor(
        public id?: number,
        public allDay?: boolean,
        public draggable?: boolean,
        public end?: Moment,
        public start?: Moment,
        public title?: Moment,
        public userLogin?: string,
        public userId?: number,
        public customerId?: number
    ) {
        this.allDay = this.allDay || false;
        this.draggable = this.draggable || false;
    }
}
