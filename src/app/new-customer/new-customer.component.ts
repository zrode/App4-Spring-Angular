import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomersService } from '../services/customers.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.css'
})
export class NewCustomerComponent implements OnInit {

  newCustomerFormGroup: FormGroup | undefined;

  constructor(private fb: FormBuilder, 
              private customerService: CustomersService, 
              private router: Router) {}

  ngOnInit(): void {
      this.newCustomerFormGroup=this.fb.group({
        name: this.fb.control("", [Validators.required]),
        email: this.fb.control("", [Validators.email, Validators.required])
      });
  }

  handleSaveCustomer() {
    let customer = this.newCustomerFormGroup?.value;
    this.customerService.saveCustomer(customer).subscribe({
      next: data => {
        alert('Customer successfuly created !!!');
        /* effacer le formulaire une fois validé 
        this.newCustomerFormGroup?.reset();
        */
       this.router.navigateByUrl("/customers");
      },
      error : erreur => {
        console.log(erreur);
      }
    });
  }

}
