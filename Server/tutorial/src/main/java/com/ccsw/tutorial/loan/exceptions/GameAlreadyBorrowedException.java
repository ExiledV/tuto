package com.ccsw.tutorial.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "This game is already borrowed between this dates")
public class GameAlreadyBorrowedException extends Exception {
    public GameAlreadyBorrowedException(String message) {
        super(message);
    }
}
