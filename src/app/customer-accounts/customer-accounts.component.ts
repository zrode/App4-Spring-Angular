import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Customer } from '../model/customer.model';

@Component({
  selector: 'app-customer-accounts',
  templateUrl: './customer-accounts.component.html',
  styleUrl: './customer-accounts.component.css'
})
export class CustomerAccountsComponent implements OnInit {

  customerId!: string;
  customer!: Customer;

  constructor(private route: ActivatedRoute,
              private router: Router
  ) {
    // je récupère mon objet customer
    this.customer=router.getCurrentNavigation()?.extras.state as Customer;
  }

  ngOnInit(): void {
      this.customerId = this.route.snapshot.params['id'];
  }
}
