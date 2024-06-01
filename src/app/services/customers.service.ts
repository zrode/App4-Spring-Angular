import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../model/customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomersService {
  /* On peut également déclarer backendHost comme variable d'environnement, beaucoup plus pratique */
  backendHost: string="http://localhost:9090";

  constructor(private http: HttpClient) { }

  public getCustomers(): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(this.backendHost+"/customers");
  }

  public searchCustomers(keyword: string): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(this.backendHost+"/customers/search?keyword="+keyword);
  }

  public saveCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(this.backendHost+"/customers", customer);
  }

  public deleteCustomer(id: number) {
    return this.http.delete<Customer>(this.backendHost+"/customers/"+id);
  }
}
