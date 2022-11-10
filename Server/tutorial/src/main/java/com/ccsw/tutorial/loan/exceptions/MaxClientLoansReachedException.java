package com.ccsw.tutorial.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "This client have been borrowed 2 games or more between this dates")
public class MaxClientLoansReachedException extends Exception {
    public MaxClientLoansReachedException(String message) {
        super(message);
    }
}