import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AccountsService } from '../services/accounts.service';
import { Observable, catchError, throwError } from 'rxjs';
import { AccountDetails } from '../model/account.model';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css'
})
export class AccountsComponent implements OnInit{

  accountFormGroup!: FormGroup;
  currentPage: number=0;
  size: number=5;
  accountObservable!: Observable<AccountDetails>; // On peut utiliser une variable de type Observable au lieu de faire un subscribe
  operationFormGroup!: FormGroup;
  errorMessage!: string;

  constructor(private fb: FormBuilder, private accountService: AccountsService) {}

  ngOnInit() {
    this.accountFormGroup = this.fb.group({
      accountId: this.fb.control('')
    });
    this.operationFormGroup = this.fb.group({
      operationType: this.fb.control(null),
      amount: this.fb.control(0),
      description: this.fb.control(null),
      accountDestination: this.fb.control(null)
    });
  }

  handleSearchAccount() {
    let accountId: string = this.accountFormGroup?.value.accountId;
    this.accountObservable = this.accountService.getAccount(accountId, this.currentPage, this.size).pipe(
      catchError(err=> {
        this.errorMessage=err.name;
        return throwError(err);
      }

      )
    );
  }

  goToPage(p: number) {
    this.currentPage=p;
    this.handleSearchAccount();
  }

  handleAccountOperation() {
    let accountId: string = this.accountFormGroup.value.accountId;
    let amount: number = this.operationFormGroup.value.amount;
    let description: string = this.operationFormGroup.value.description;
    let operationType = this.operationFormGroup.value.operationType;

    if(operationType=='DEBIT') {
      this.accountService.debit(accountId, amount, description).subscribe({
        next: (data)=> {
          console.log("Debit Success");
        },
        error: (erreur)=> {
          console.log(erreur);
        }
      });
    } else if(operationType=='CREDIT') {
      this.accountService.credit(this.accountFormGroup.value.accountId, this.operationFormGroup.value.amount, this.operationFormGroup.value.description).subscribe({
        next: (data)=> {
          console.log("Credit Success");
        },
        error: (erreur)=> {
          console.log(erreur);
        }
      });      
    } else if(operationType=='TRANSFER') {
      this.accountService.transfer(this.accountFormGroup.value.accountId, this.operationFormGroup.value.accountDestination ,this.operationFormGroup.value.amount).subscribe({
        next: (data)=> {
          console.log("Transfer Success");
        },
        error: (erreur)=> {
          console.log(erreur);
        }
      });      
    }
    this.operationFormGroup.reset();
  }
}
