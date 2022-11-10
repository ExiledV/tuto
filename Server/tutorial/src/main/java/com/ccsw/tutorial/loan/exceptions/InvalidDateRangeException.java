package com.ccsw.tutorial.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Date ranges are invalid (Max loan days 14)")
public class InvalidDateRangeException extends Exception {
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
