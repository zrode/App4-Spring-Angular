import { Component, OnInit } from '@angular/core';
import { CustomersService } from '../services/customers.service';
import { Observable, catchError, throwError } from 'rxjs';
import { Customer } from '../model/customer.model';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit {

  customers!: Observable<Array<Customer>>;
  errorMessage!: string;

  constructor(private customerService: CustomersService) {}

  ngOnInit(): void {
    this.customers = this.customerService.getCustomers().pipe(
      catchError(erreur => {
        this.errorMessage = erreur.message;
        return throwError(erreur);
      })
    );
  }

}
