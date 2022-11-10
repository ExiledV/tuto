package com.ccsw.tutorial.client;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccsw.tutorial.client.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByName(String name);
}
