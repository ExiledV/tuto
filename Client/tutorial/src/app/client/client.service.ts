import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, tap } from 'rxjs';
import { Client } from './model/Client';
import { map } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private httpClient: HttpClient) { }

  getClients(): Observable<Client[]> {
    return this.httpClient.get<Client[]>('http://localhost:8080/clients');
  }

  saveClient(client: Client): Observable<Client>{
    let url = 'http://localhost:8080/clients/';
    if(client.id != null)
      url += client.id;
    
    return this.httpClient.put<Client>(url, client);
  }

  deleteClient(idClient: number): Observable<any>{
    return this.httpClient.delete('http://localhost:8080/clients/' + idClient);
  }
}
