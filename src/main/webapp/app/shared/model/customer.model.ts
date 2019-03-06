export interface ICustomer {
    id?: number;
    name?: string;
    addressId?: number;
    userLogin?: string;
    userId?: number;
}

export class Customer implements ICustomer {
    constructor(public id?: number, public name?: string, public addressId?: number, public userLogin?: string, public userId?: number) {}
}
