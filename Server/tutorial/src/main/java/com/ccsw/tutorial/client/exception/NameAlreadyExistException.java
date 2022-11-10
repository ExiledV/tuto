package com.ccsw.tutorial.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Client name already exist.")
public class NameAlreadyExistException extends Exception {
    public NameAlreadyExistException(String message) {
        super(message);
    }
}