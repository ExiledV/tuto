package com.ccsw.tutorial.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.tutorial.client.exception.NameAlreadyExistException;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.config.mapper.BeanMapper;

/**
 * @author rgomezbe
 */
@RequestMapping(value = "/clients")
@RestController
@CrossOrigin(origins = "*")
public class ClientContoller {

    @Autowired
    ClientService clientService;

    @Autowired
    BeanMapper beanMapper;

    /**
     * Método para recuperar todas las {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @return
     */
    @RequestMapping(path = "", method = RequestMethod.GET)
    List<ClientDto> findAll() {
        return beanMapper.mapList(this.clientService.findAll(), ClientDto.class);
    }

    /**
     * Método para crear o actualizar una
     * {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @param dto
     * @throws NameAlreadyExistException
     */
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    void save(@PathVariable(name = "id", required = false) Long id, @RequestBody ClientDto dto)
            throws NameAlreadyExistException {
        this.clientService.save(id, dto);
    }

    /**
     * Método para borrar una {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @param id
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    void delete(@PathVariable(name = "id") Long id) {
        this.clientService.delete(id);
    }
}
