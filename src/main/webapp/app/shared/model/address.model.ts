export interface IAddress {
    id?: number;
    streetNumber?: string;
    route?: string;
    route2?: string;
    postalCode?: string;
    city?: string;
}

export class Address implements IAddress {
    constructor(
        public id?: number,
        public streetNumber?: string,
        public route?: string,
        public route2?: string,
        public postalCode?: string,
        public city?: string
    ) {}
}
