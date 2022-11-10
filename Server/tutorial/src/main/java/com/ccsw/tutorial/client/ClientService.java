package com.ccsw.tutorial.client;

import java.util.List;

import com.ccsw.tutorial.client.exception.NameAlreadyExistException;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

public interface ClientService {

    /**
     * Método para recuperar todas las {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @return
     */
    List<Client> findAll();

    /**
     * Método para crear o actualizar una
     * {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @param dto
     * @throws NameAlreadyExistException
     */
    void save(Long id, ClientDto dto) throws NameAlreadyExistException;

    /**
     * Método para borrar una {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @param id
     */
    void delete(Long id);

    /**
     * Método que devuelve un cliente especificado
     * 
     * @param id
     */
    Client get(Long id);
}
