package com.ccsw.tutorial.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.ccsw.tutorial.client.model.ClientDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/clients/";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<ClientDto>> responseType = new ParameterizedTypeReference<List<ClientDto>>() {
    };

    @Test
    public void findAllShouldReturnAllClients() {
        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(6, response.getBody().size());
    }

    static final Long NOT_EXISTING_CLIENT_ID = 20L;
    static final String NOT_EXISTING_CLIENT_NAME = "ClienteX";
    static final String EXISTING_CLIENT_NAME = "Cliente 3";

    @Test
    public void saveWithoutIdWithNotExistingNameShouldInsert() {
        ClientDto dto = new ClientDto();
        dto.setName(NOT_EXISTING_CLIENT_NAME);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(7, response.getBody().size());

        ClientDto clientSearch = response.getBody().stream().filter(item -> item.getId().equals(7L)).findFirst()
                .orElse(null);

        assertNotNull(clientSearch);
        assertEquals(NOT_EXISTING_CLIENT_NAME, clientSearch.getName());
    }

    @Test
    public void saveWithoutIdWithExistingNameShouldReturnInternalError() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(EXISTING_CLIENT_NAME);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(clientDto), Void.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    static final Long EXISTING_CLIENT_ID = 1L;

    @Test
    public void saveWithExistingIdWithNotExistingNameShouldUpdate() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(NOT_EXISTING_CLIENT_NAME);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXISTING_CLIENT_ID, HttpMethod.PUT,
                new HttpEntity<>(clientDto), Void.class);

        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(6, response.getBody().size());

        ClientDto clientSearch = response.getBody().stream().filter(item -> item.getId().equals(EXISTING_CLIENT_ID))
                .findFirst().orElse(null);

        assertNotNull(clientSearch);
        assertEquals(NOT_EXISTING_CLIENT_NAME, clientSearch.getName());
    }

    @Test
    public void saveWithExistingIdWithExistingNameShouldReturnInternalError() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(EXISTING_CLIENT_NAME);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXISTING_CLIENT_ID,
                HttpMethod.PUT, new HttpEntity<>(clientDto), Void.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void deleteWithExistingIdShouldDelete() {
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXISTING_CLIENT_ID, HttpMethod.DELETE, null,
                Void.class);

        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(5, response.getBody().size());
    }

    public void deleteWithoutExistingIdShouldReturnInternalError() {
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + NOT_EXISTING_CLIENT_ID,
                HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
