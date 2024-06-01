import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountDetails } from '../model/account.model';

@Injectable({
  providedIn: 'root'
})
export class AccountsService {

  backendHost: string="http://localhost:9090";

  constructor(private http: HttpClient) { }

  public getAccount(accountId: string, page: number, size: number): Observable<AccountDetails> {
    return this.http.get<AccountDetails>(this.backendHost+"/accounts/"+accountId+"/operations?page="+page+"&size="+size);
  }

  public debit(accountId: string, amount: number, description: string) {
    let data = {accountId: accountId, amount: amount, description: description};
    return this.http.post(this.backendHost+"/accounts/debit", data);
  }

  public credit(accountId: string, amount: number, description: string) {
    let data = {accountId: accountId, amount: amount, description: description};
    return this.http.post(this.backendHost+"/accounts/credit", data);
  }


  public transfer(accountSource: string, accountDestination: string, amount: number) {
    let data = {accountSource: accountSource, accountDestination: accountDestination,amount: amount};
    return this.http.post(this.backendHost+"/accounts/transfer", data);
  }

}
