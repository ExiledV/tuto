package com.ccsw.tutorial.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.tutorial.client.exception.NameAlreadyExistException;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    /**
     * Método para recuperar todas las {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @return
     */
    @Override
    public List<Client> findAll() {
        return (List<Client>) clientRepository.findAll();
    }

    /**
     * Método para crear o actualizar una
     * {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @param dto
     */
    @Override
    public void save(Long id, ClientDto dto) throws NameAlreadyExistException {
        Client client = null;

        if (id == null)
            client = new Client();
        else
            client = this.clientRepository.findById(id).orElse(null);

        if (this.clientRepository.findByName(dto.getName()).orElse(null) == null) {
            client.setName(dto.getName());
            this.clientRepository.save(client);
        } else
            throw new NameAlreadyExistException("Client name already exist.");
    }

    /**
     * Método para borrar una {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @param id
     */
    @Override
    public void delete(Long id) {
        this.clientRepository.deleteById(id);
    }

    /**
     * Método que devuelve un cliente especificado
     * 
     * @param id
     */
    public Client get(Long id) {
        return this.clientRepository.findById(id).orElse(null);
    }
}
