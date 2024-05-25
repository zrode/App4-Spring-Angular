import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../model/customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomersService {

  constructor(private http: HttpClient) { }

  public getCustomers(): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>("http://localhost:9090/customers");
  }
}
