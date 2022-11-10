package com.ccsw.tutorial.loan;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.exceptions.GameAlreadyBorrowedException;
import com.ccsw.tutorial.loan.exceptions.InvalidDateRangeException;
import com.ccsw.tutorial.loan.exceptions.MaxClientLoansReachedException;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

/**
 * @author ccsw
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    GameService gameService;

    @Override
    public Page<Loan> findPage(LoanSearchDto dto, Long game, Long client, Date date) {
        return this.loanRepository.find(dto.getPageable(), game, client, date);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws CantSaveLoanException
     * 
     * @throws GameAlreadyBorrowedException
     */
    @Override
    public void save(LoanDto dto)
            throws GameAlreadyBorrowedException, MaxClientLoansReachedException, InvalidDateRangeException {

        if (checkValidDateRange(dto.getLoanDate(), dto.getReturnDate()))
            if (checkLoansInDateRange(dto.getClient(), dto.getLoanDate(), dto.getReturnDate()))
                if (checkGameBorrowedInDateRange(dto.getGame(), dto.getLoanDate(), dto.getReturnDate())) {
                    Loan loan = new Loan();
                    BeanUtils.copyProperties(dto, loan, "id", "game", "client");

                    loan.setClient(this.clientService.get(dto.getClient().getId()));
                    loan.setGame(this.gameService.get(dto.getGame().getId()));

                    this.loanRepository.save(loan);
                } else
                    throw new GameAlreadyBorrowedException("This game is already borrowed between this dates");
            else
                throw new MaxClientLoansReachedException("This client can not loan between this dates");
        else
            throw new InvalidDateRangeException("La fecha es inválida o supera los 14 dias de reserva");

    }

    @Override
    public void delete(Long id) {
        this.loanRepository.deleteById(id);
    }

    public boolean checkValidDateRange(Date loanDate, Date returnDate) {
        boolean ok = false;

        if (returnDate.after(loanDate)) {
            long diffInMillies = Math.abs(returnDate.getTime() - loanDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diff <= 14)
                ok = true;
        }

        return ok;
    }

    public boolean checkLoansInDateRange(ClientDto client, Date loanDate, Date returnDate) {

        List<Loan> loans = this.loanRepository.findByClientId(client.getId());
        int loansSize = loans.size(), pos = 0, count = 0;
        boolean res = true;

        Date auxLoanDate;
        Date auxReturnDate;

        while (pos < loansSize && count < 2) {
            auxLoanDate = loans.get(pos).getLoanDate();
            auxReturnDate = loans.get(pos).getReturnDate();

            // Comprueba si las fechas se colapsan entre si
            if ((auxLoanDate.compareTo(loanDate) <= 0) && auxReturnDate.compareTo(returnDate) >= 0) {
                count++;
            } else {
                if ((auxLoanDate.compareTo(loanDate) >= 0)
                        && (auxReturnDate.compareTo(returnDate) >= 0 && auxLoanDate.compareTo(returnDate) <= 0))
                    count++;
                else {
                    if (((auxLoanDate.compareTo(loanDate) <= 0) && auxReturnDate.compareTo(loanDate) >= 0)
                            && (auxReturnDate.compareTo(returnDate) <= 0))
                        count++;
                    else {
                        if ((auxLoanDate.compareTo(loanDate) >= 0) && auxReturnDate.compareTo(returnDate) < 0)
                            count++;
                    }
                }
            }
            pos++;
        }

        if (count >= 2)
            res = false;

        return res;
    }

    public boolean checkGameBorrowedInDateRange(GameDto game, Date loanDate, Date returnDate) {

        List<Loan> loans = this.loanRepository.findByGameId(game.getId());
        int loansSize = loans.size(), pos = 0;
        boolean res = true;

        Date auxLoanDate;
        Date auxReturnDate;

        while (pos < loansSize && res) {
            auxLoanDate = loans.get(pos).getLoanDate();
            auxReturnDate = loans.get(pos).getReturnDate();

            // Comprueba si las fechas se colapsan entre si
            if ((auxLoanDate.compareTo(loanDate) <= 0) && auxReturnDate.compareTo(returnDate) >= 0) {
                res = false;
            } else {
                if ((auxLoanDate.compareTo(loanDate) >= 0)
                        && (auxReturnDate.compareTo(returnDate) >= 0 && auxLoanDate.compareTo(returnDate) <= 0))
                    res = false;
                else {
                    if (((auxLoanDate.compareTo(loanDate) <= 0) && auxReturnDate.compareTo(loanDate) >= 0)
                            && (auxReturnDate.compareTo(returnDate) <= 0))
                        res = false;
                    else {
                        if ((auxLoanDate.compareTo(loanDate) >= 0) && auxReturnDate.compareTo(returnDate) <= 0)
                            res = false;
                    }
                }
            }

            pos++;

        }

        return res;
    }
}
