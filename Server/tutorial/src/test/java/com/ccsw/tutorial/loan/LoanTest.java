package com.ccsw.tutorial.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.exceptions.GameAlreadyBorrowedException;
import com.ccsw.tutorial.loan.exceptions.InvalidDateRangeException;
import com.ccsw.tutorial.loan.exceptions.MaxClientLoansReachedException;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;

@SuppressWarnings("deprecation")
@ExtendWith(MockitoExtension.class)
public class LoanTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private GameService gameService;

    @InjectMocks
    private LoanServiceImpl loanService;

    public static final Long EXISTING_LOAN_GAME_ID = 6L;
    public static final Long EXISTING_LOAN_CLIENT_ID = 4L;

    public static final Long LOAN_CLIENT_ID_1 = 1L;
    public static final Long LOAN_GAME_ID_1 = 1L;

    @Test
    public void saveWithInvalidRangeShouldReturnInvalidDateRangeException() {
        LoanDto loanDto = new LoanDto();

        GameDto game = new GameDto();
        ClientDto client = new ClientDto();

        game.setId(EXISTING_LOAN_GAME_ID);
        client.setId(EXISTING_LOAN_CLIENT_ID);

        loanDto.setGame(game);
        loanDto.setClient(client);
        loanDto.setLoanDate(new Date(2022 - 1900, 11 - 1, 3));
        loanDto.setReturnDate(new Date(2022 - 1900, 11 - 1, 2));

        // Comprueba si no ha hecho que
        assertThrows(InvalidDateRangeException.class, () -> {
            this.loanService.save(loanDto);
        });

        loanDto.setLoanDate(new Date(2022 - 1900, 11 - 1, 3));
        loanDto.setReturnDate(new Date(2022 - 1900, 11 - 1, 30));

        assertThrows(InvalidDateRangeException.class, () -> {
            this.loanService.save(loanDto);
        });
    }

    @Test
    public void saveValidLoanShouldInsert()
            throws GameAlreadyBorrowedException, MaxClientLoansReachedException, InvalidDateRangeException {

        ArgumentCaptor<Loan> loan = ArgumentCaptor.forClass(Loan.class);

        List<Loan> mockedLoanList = new ArrayList<Loan>();

        LoanDto loanDto = new LoanDto();

        GameDto gameDto = mock(GameDto.class);
        ClientDto clientDto = mock(ClientDto.class);

        Game game = mock(Game.class);
        Client client = mock(Client.class);

        when(gameDto.getId()).thenReturn(EXISTING_LOAN_GAME_ID);
        when(clientDto.getId()).thenReturn(EXISTING_LOAN_CLIENT_ID);
        when(game.getId()).thenReturn(EXISTING_LOAN_GAME_ID);
        when(client.getId()).thenReturn(EXISTING_LOAN_CLIENT_ID);

        when(this.clientService.get(EXISTING_LOAN_CLIENT_ID)).thenReturn(client);
        when(this.gameService.get(EXISTING_LOAN_GAME_ID)).thenReturn(game);

        loanDto.setGame(gameDto);
        loanDto.setClient(clientDto);

        loanDto.setLoanDate(new Date(2022 - 1900, 11 - 1, 3));
        loanDto.setReturnDate(new Date(2022 - 1900, 11 - 1, 13));

        when(this.loanRepository.findByClientId(EXISTING_LOAN_CLIENT_ID)).thenReturn(mockedLoanList);
        when(this.loanRepository.findByGameId(EXISTING_LOAN_GAME_ID)).thenReturn(mockedLoanList);

        this.loanService.save(loanDto);

        verify(loanRepository).save(loan.capture());

        assertEquals(EXISTING_LOAN_GAME_ID, loan.getValue().getGame().getId());
        assertEquals(EXISTING_LOAN_CLIENT_ID, loan.getValue().getClient().getId());
        assertEquals(loanDto.getLoanDate(), loan.getValue().getLoanDate());
        assertEquals(loanDto.getReturnDate(), loan.getValue().getReturnDate());
    }

    @Test
    public void saveNotValidNumberOfLoansInRangeShouldThrowMaxClientLoansReachedException() {

        LoanDto loanDto = new LoanDto();

        List<Loan> mockedLoanList = new ArrayList<Loan>();

        Loan mockedLoan1 = mock(Loan.class), mockedLoan2 = mock(Loan.class);

        mockedLoanList.add(mockedLoan1);
        mockedLoanList.add(mockedLoan2);

        when(mockedLoan1.getLoanDate()).thenReturn(new Date(2022 - 1900, 11 - 1, 3));
        when(mockedLoan1.getReturnDate()).thenReturn(new Date(2022 - 1900, 11 - 1, 13));
        when(mockedLoan2.getLoanDate()).thenReturn(new Date(2022 - 1900, 11 - 1, 1));
        when(mockedLoan2.getReturnDate()).thenReturn(new Date(2022 - 1900, 11 - 1, 10));

        GameDto game = new GameDto();
        ClientDto client = new ClientDto();

        game.setId(EXISTING_LOAN_GAME_ID);
        client.setId(LOAN_CLIENT_ID_1);

        loanDto.setGame(game);
        loanDto.setClient(client);
        loanDto.setLoanDate(new Date(2022 - 1900, 11 - 1, 3));
        loanDto.setReturnDate(new Date(2022 - 1900, 11 - 1, 13));

        when(this.loanRepository.findByClientId(LOAN_CLIENT_ID_1)).thenReturn(mockedLoanList);

        assertThrows(MaxClientLoansReachedException.class, () -> {
            this.loanService.save(loanDto);
        });
    }

    @Test
    public void saveWithBorrowerdGameShouldThrowGameAlreadyBorrowedException() {

        LoanDto loanDto = new LoanDto();

        List<Loan> mockedLoanListGame = new ArrayList<Loan>();
        List<Loan> mockedLoanListClient = new ArrayList<Loan>();

        Loan mockedLoan1 = mock(Loan.class);

        mockedLoanListGame.add(mockedLoan1);

        when(mockedLoan1.getLoanDate()).thenReturn(new Date(2022 - 1900, 11 - 1, 10));
        when(mockedLoan1.getReturnDate()).thenReturn(new Date(2022 - 1900, 11 - 1, 13));

        when(this.loanRepository.findByClientId(EXISTING_LOAN_CLIENT_ID)).thenReturn(mockedLoanListClient);
        when(this.loanRepository.findByGameId(LOAN_GAME_ID_1)).thenReturn(mockedLoanListGame);

        GameDto game = new GameDto();
        ClientDto client = new ClientDto();

        game.setId(LOAN_GAME_ID_1);
        client.setId(EXISTING_LOAN_CLIENT_ID);

        loanDto.setGame(game);
        loanDto.setClient(client);

        loanDto.setLoanDate(new Date(2022 - 1900, 11 - 1, 3));
        loanDto.setReturnDate(new Date(2022 - 1900, 11 - 1, 13));

        assertThrows(GameAlreadyBorrowedException.class, () -> {
            this.loanService.save(loanDto);
        });
    }

    @Test
    public void ifReturnDateIsBeforeLoanDateShouldReturnFalse() {
        boolean ok;

        Date loanDate = new Date(2022 - 1900, 11 - 1, 3);
        Date returnDate = new Date(2022 - 1900, 11 - 1, 2);

        ok = loanService.checkValidDateRange(loanDate, returnDate);

        assertEquals(ok, false);
    }

    @Test
    public void ifMoreThan14DaysDateRangeShouldReturnFalse() {
        boolean ok;

        Date loanDate;
        Date returnDate;
        loanDate = new Date(2022 - 1900, 11 - 1, 3);
        returnDate = new Date(2022 - 1900, 11 - 1, 30);

        ok = loanService.checkValidDateRange(loanDate, returnDate);

        assertEquals(ok, false);

    }

    @Test
    public void ifValidDateShouldReturnTrue() {
        boolean ok;

        Date loanDate;
        Date returnDate;
        loanDate = new Date(2022 - 1900, 11 - 1, 3);
        returnDate = new Date(2022 - 1900, 11 - 1, 10);

        ok = loanService.checkValidDateRange(loanDate, returnDate);

        assertTrue(ok);

    }

}
