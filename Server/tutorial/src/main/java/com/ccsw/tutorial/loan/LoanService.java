package com.ccsw.tutorial.loan;

import java.util.Date;

import org.springframework.data.domain.Page;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.exceptions.GameAlreadyBorrowedException;
import com.ccsw.tutorial.loan.exceptions.InvalidDateRangeException;
import com.ccsw.tutorial.loan.exceptions.MaxClientLoansReachedException;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

public interface LoanService {

    /**
     * Método para recuperar un listado paginado con filtros de
     * {@link com.ccsw.tutorial.author.model.Loan}
     * 
     * @param dto
     * @return
     */
    Page<Loan> findPage(LoanSearchDto dto, Long gameId, Long clientId, Date date);

    /**
     * Método para crear o actualizar un {@link com.ccsw.tutorial.author.model.Loan}
     * 
     * @param id
     * @param data
     * @throws CantSaveLoanException
     */
    void save(LoanDto data)
            throws GameAlreadyBorrowedException, MaxClientLoansReachedException, InvalidDateRangeException;

    /**
     * Método para crear o actualizar un
     * {@link com.ccsw.tutorial.author.model.Author}
     * 
     * @param id
     */
    void delete(Long id);

    public boolean checkValidDateRange(Date loanDate, Date returnDate);

    public boolean checkLoansInDateRange(ClientDto client, Date loanDate, Date returnDate);

    public boolean checkGameBorrowedInDateRange(GameDto game, Date loanDate, Date returnDate);
}
