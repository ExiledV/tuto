import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ClientService } from 'src/app/client/client.service';
import { Client } from 'src/app/client/model/Client';
import { GameService } from 'src/app/game/game.service';
import { Game } from 'src/app/game/model/Game';
import { LoanService } from '../loan.service';
import { Loan } from '../model/Loan';

@Component({
  selector: 'app-loan-new',
  templateUrl: './loan-new.component.html',
  styleUrls: ['./loan-new.component.scss']
})
export class LoanNewComponent implements OnInit {

  games: Game[];
  clients: Client[];
  loan: Loan;

  loanDate = new FormControl(new Date().toISOString);
  returnDate = new FormControl(new Date().toISOString);

  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });


  constructor(
    public dialogRef: MatDialogRef<LoanNewComponent>,
    private loanService: LoanService,
    private gameService: GameService,
    private clientService: ClientService
  ) { }

  ngOnInit(): void {
    this.loan = new Loan();
    this.gameService.getGames().subscribe( games => {
      this.games = games;
    });

    this.clientService.getClients().subscribe( clients => {
        this.clients = clients;
    });
  }

  onClose(){
    this.dialogRef.close();
  }

  onSave(){
    this.loanService.saveLoan(this.loan).subscribe( result => {
      this.dialogRef.close();
    })
  }


}
