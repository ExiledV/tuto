package com.ccsw.tutorial.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.ccsw.tutorial.author.model.AuthorSearchDto;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {
    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loans";

    private static final int TOTAL_LOANS = 6;
    private static final int PAGE_SIZE = 5;

    // Esto se hace dentro para poder hacer el try/catch
    @SuppressWarnings("deprecation")
    private static final Date FIRST_DECEMBER_LOAN_DATE = new Date(2022 - 1900, 12 - 1, 1);
    @SuppressWarnings("deprecation")
    private static final Date SEVENTH_DECEMBER_RETURN_DATE = new Date(2022 - 1900, 12 - 1, 7);
    @SuppressWarnings("deprecation")
    private static final Date FIRST_NOVEMBER_LOAN_DATE = new Date(2022 - 1900, 11 - 1, 1);
    @SuppressWarnings("deprecation")
    private static final Date MID_DECEMBER_RETURN_DATE = new Date(2022 - 1900, 12 - 1, 16);
    @SuppressWarnings("deprecation")
    private static final Date TENTH_NOVEMBER_RETURN_DATE = new Date(2022 - 1900, 11 - 1, 10);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<Page<LoanDto>> responseTypePage = new ParameterizedTypeReference<Page<LoanDto>>() {
    };

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        AuthorSearchDto searchDto = new AuthorSearchDto();
        searchDto.setPageable(PageRequest.of(0, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_LOANS - PAGE_SIZE;

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void saveValidLoanShouldInsert() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setName("Cliente 1");

        LoanDto loanDto = new LoanDto(gameDto, clientDto, FIRST_DECEMBER_LOAN_DATE, SEVENTH_DECEMBER_RETURN_DATE);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(0, PAGE_SIZE));

        ResponseEntity<?> res = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(loanDto), Void.class);

        assertEquals(HttpStatus.OK, res.getStatusCode());

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals((TOTAL_LOANS + 1), response.getBody().getTotalElements());
    }

    @Test
    public void saveWithInvalidRangeShouldThrowInvalidDateRangeException() {

        GameDto game = new GameDto();
        ClientDto client = new ClientDto();

        game.setId(1L);
        client.setId(1L);

        // This tests when return date is before loan date
        LoanDto dto = new LoanDto(game, client, FIRST_DECEMBER_LOAN_DATE, TENTH_NOVEMBER_RETURN_DATE);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(dto), Void.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        // assertEquals(response.getBody().getMessage());

        dto.setReturnDate(MID_DECEMBER_RETURN_DATE); // This tests when the range of days is 15

        response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto),
                Void.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

    }

    @Test
    public void saveWithMaxLoansShouldThrowMaxClientLoansReachedException() {

        GameDto game = new GameDto();
        ClientDto client = new ClientDto();

        game.setId(6L);
        client.setId(1L);
        // Tests when a client is going to loan more than 2 games in a date range
        LoanDto dto = new LoanDto(game, client, FIRST_NOVEMBER_LOAN_DATE, TENTH_NOVEMBER_RETURN_DATE);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        // assertEquals(response.getBody().getMessage());

    }

    @Test
    public void saveABorrowedGameShouldThrowGameAlreadyBorrowedException() {

        GameDto game = new GameDto();
        ClientDto client = new ClientDto();

        game.setId(1L);
        client.setId(2L);

        LoanDto dto = new LoanDto(game, client, FIRST_NOVEMBER_LOAN_DATE, TENTH_NOVEMBER_RETURN_DATE);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(dto), Void.class);

        assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
        // assertEquals(response.getBody().getMessage());

    }

    static final Long EXISTING_ID = 1L;

    @Test
    public void deleteExistingIdShouldDelete() {
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTING_ID, HttpMethod.DELETE, null, Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertEquals(TOTAL_LOANS - 1, response.getBody().getTotalElements());

        LoanDto loan = response.getBody().getContent().stream().filter(item -> item.getId().equals(EXISTING_ID))
                .findFirst().orElse(null);

        assertNull(loan);
    }

}
