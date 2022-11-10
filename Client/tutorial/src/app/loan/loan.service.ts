import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { yearsPerPage } from '@angular/material/datepicker';
import { filter, Observable, of } from 'rxjs';
import { Pageable } from '../core/model/page/Pageable';
import { Loan } from './model/Loan';
import { LoanPage } from './model/LoanPage';

@Injectable({
  providedIn: 'root'
})
export class LoanService {



  constructor(
    private http: HttpClient
  ) { }

  getLoans(pageable: Pageable, gameId?: number, clientId?: number, filterDate?: Date): Observable<LoanPage> {
    return this.http.post<LoanPage>(this.composeFindUrl(gameId, clientId, filterDate), {pageable:pageable});
  }

  saveLoan(loan: Loan): Observable<void> {
    return this.http.put<void>('http://localhost:8080/loans', loan);
  }

  deleteLoan(idLoan : number): Observable<void> {
    return this.http.delete<void>('http://localhost:8080/loans/' + idLoan);
  }
  
  private composeFindUrl(gameId?: number, clientId?: number, filterDate?: Date) : string {
    let params = '';

    if (gameId != null) {
        params += 'gameId='+gameId;
    }

    if (clientId != null) {
        if (params != '') params += "&";
        params += "clientId="+clientId;
    }

    if (filterDate != null) {
      if (params != '') params += "&";
      params += "filterDate="+filterDate.getDate() + "/"+ (filterDate.getMonth() + 1) + "/" + filterDate.getFullYear();
    }

    let url = 'http://localhost:8080/loans'

    if (params == '') return url;
    else return url + '?'+params;
  }

}
