package com.ccsw.tutorial.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ccsw.tutorial.client.exception.NameAlreadyExistException;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

@ExtendWith(MockitoExtension.class)
public class ClientTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void findAllClientsShouldReturnAllCients() {
        List<Client> list = new ArrayList<>();
        list.add(mock(Client.class));

        when(clientRepository.findAll()).thenReturn(list);

        List<Client> clients = clientService.findAll();

        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    public static final String CLIENT_NAME = "ClientX";

    @Test
    public void saveNotExistingClientWithNotExistingNameShouldInsert() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(CLIENT_NAME);

        ArgumentCaptor<Client> client = ArgumentCaptor.forClass(Client.class);
        when(clientRepository.findByName(CLIENT_NAME)).thenReturn(null);

        try {
            this.clientService.save(null, clientDto);
        } catch (NameAlreadyExistException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        verify(clientRepository).save(client.capture());

        assertEquals(CLIENT_NAME, client.getValue().getName());
    }

    public static final Long CLIENT_ID = 1L;

    @Test
    public void saveExistingClientWithNotExistingNameShouldUpdate() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(CLIENT_NAME);

        Client client = mock(Client.class);

        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
        when(clientRepository.findByName(CLIENT_NAME)).thenReturn(null);

        try {
            clientService.save(CLIENT_ID, clientDto);
        } catch (NameAlreadyExistException e) {
        }

        verify(clientRepository).save(client);
    }

    static final String EXISTING_NAME = "Cliente 1";

    @Test
    public void deleteExistingClientShouldDelete() {
        clientService.delete(CLIENT_ID);

        verify(clientRepository).deleteById(CLIENT_ID);
    }

    /*
     * 
     * public void getExistingClientShouldReturnClient() { Client client =
     * mock(Client.class);
     * 
     * when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
     * 
     * Client clientResponse = clientService.get(CLIENT_ID);
     * 
     * assertNotNull(clientResponse); }
     * 
     * static final Long NOT_EXISTING_CLIENT_ID = 20L;
     * 
     * public void getExistingClientShouldReturnClient() { Client client =
     * mock(Client.class);
     * 
     * when(clientRepository.findById(NOT_EXISTING_CLIENT_ID)).thenReturn(Optional.
     * empty());
     * 
     * Client clientResponse = clientService.get(NOT_EXISTING_CLIENT_ID);
     * 
     * assertNull(clientResponse); }
     */

}
