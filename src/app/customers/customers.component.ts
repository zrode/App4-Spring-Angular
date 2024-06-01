import { Component, NgModule, OnInit } from '@angular/core';
import { CustomersService } from '../services/customers.service';
import { Observable, catchError, throwError } from 'rxjs';
import { Customer } from '../model/customer.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Conditional } from '@angular/compiler';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})

export class CustomersComponent implements OnInit {

  customers!: Observable<Array<Customer>>;
  errorMessage!: string;
  searchFormGroup: FormGroup | undefined;

  constructor(private customerService: CustomersService, private fb: FormBuilder, private router:Router) {}

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control("")
    });

    this.customers = this.customerService.getCustomers().pipe(
      catchError(erreur => {
        this.errorMessage = erreur.message;
        return throwError(erreur);
      })
    );
  }

  handleSearchCustomers() {
    let kw = this.searchFormGroup?.value.keyword;
    this.customers = this.customerService.searchCustomers(kw).pipe(
      catchError(erreur => {
        this.errorMessage = erreur.message;
        return throwError(erreur);
      })
    );
  }

  handleDeleteCustomer(c: Customer) {
    let confirmation = confirm("Are you sure ?");
    if(!confirmation) return;
    this.customerService.deleteCustomer(c.id).subscribe({
      next: (resp) => {
        /* this.handleSearchCustomers(); */
        alert('Customer deleted!');
        this.router.navigateByUrl("/customers");
      },
      error: erreur => {
        console.log(erreur);
      }
    });
  }

  handleCustomerAccounts(customer: Customer) {
    this.router.navigateByUrl("/customer-accounts/"+customer.id, {state: customer}); // je transmet l'objet grâce à state
  }

}
