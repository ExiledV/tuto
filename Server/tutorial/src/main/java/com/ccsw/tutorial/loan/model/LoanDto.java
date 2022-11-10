package com.ccsw.tutorial.loan.model;

import java.util.Date;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;

public class LoanDto {

    private Long id;

    private GameDto game;

    private ClientDto client;

    private Date loanDate, returnDate;

    public LoanDto(GameDto game, ClientDto client, Date loanDate, Date returnDate) {
        this.game = game;
        this.client = client;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public LoanDto() {
    }

    /**
     * Getters and setters
     * 
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

}
