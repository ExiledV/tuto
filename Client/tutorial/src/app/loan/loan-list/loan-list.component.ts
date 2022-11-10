import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Client } from 'src/app/client/model/Client';
import { Pageable } from 'src/app/core/model/page/Pageable';
import { Game } from 'src/app/game/model/Game';
import { Loan } from '../model/Loan';
import { LoanService } from '../loan.service';
import { LOANS_DATA } from '../model/mock-loans';
import { MatDialog } from '@angular/material/dialog';
import { LoanNewComponent } from '../loan-new/loan-new.component';
import { GameService } from 'src/app/game/game.service';
import { ClientService } from 'src/app/client/client.service';

@Component({
  selector: 'app-loan-list',
  templateUrl: './loan-list.component.html',
  styleUrls: ['./loan-list.component.scss']
})
export class LoanListComponent implements OnInit {

  pageNumber: number = 0;
  pageSize: number = 5;
  totalElements: number = 0;

  games: Game[];
  clients: Client[];

  //Data que recogen los imput
  filtersEnabled: boolean = false;
  filterGame: Game;
  filterClient: Client;
  filterDate: Date;

  clientId: number;
  gameId: number;
  date: Date;

  
  dataSource = new MatTableDataSource<Loan>();
  displayedColumns: String[] = ["id", "game", "client", "loanDate", "returnDate"];
  constructor( 
    private loanService: LoanService,
    public dialog: MatDialog,
    private gameService: GameService,
    private clientService: ClientService
   ) { }

  ngOnInit(): void {
    this.loadPage();

    this.clientService.getClients().subscribe( clients => {
      this.clients = clients;
    });

    this.gameService.getGames().subscribe( games => {
      this.games = games;
    })
  }

  loadPage(event?: PageEvent){
    let pageable : Pageable =  {
      pageNumber: this.pageNumber,
      pageSize: this.pageSize,
      sort: [{
          property: 'id',
          direction: 'ASC'
      }]
    }

  if (event != null) {
      pageable.pageSize = event.pageSize
      pageable.pageNumber = event.pageIndex;
  }

  
    if(this.filtersEnabled){
      this.loanService.getLoans(pageable, this.gameId, this.clientId, this.date).subscribe(data => {
        this.dataSource.data = data.content;
        this.pageNumber = data.pageable.pageNumber;
        this.pageSize = data.pageable.pageSize;
        this.totalElements = data.totalElements;
    });
    }else{
      this.loanService.getLoans(pageable).subscribe(data => {
          this.dataSource.data = data.content;
          this.pageNumber = data.pageable.pageNumber;
          this.pageSize = data.pageable.pageSize;
          this.totalElements = data.totalElements;
      });
    }
  }

  createLoan(){
    const dialogRef = this.dialog.open(LoanNewComponent, {
      data: {}
    });

    dialogRef.afterClosed().subscribe( result => {
      this.ngOnInit();
    });
  }

  onCleanFilter(){
    this.filterClient = null;
    this.filterGame = null;
    this.filterDate = null;

    this.filtersEnabled = false;
  }

  onSearch(event?: PageEvent){
    this.filtersEnabled = true;

    this.clientId = this.filterClient != null ? this.filterClient.id : null;
    this.gameId = this.filterGame != null ? this.filterGame.id : null;
    this.date = this.filterDate != null ? this.filterDate : null;

    this.loadPage();
  }



}
