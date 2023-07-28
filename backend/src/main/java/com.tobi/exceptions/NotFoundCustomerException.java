package com.tobi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundCustomerException extends RuntimeException {
    public NotFoundCustomerException() {
        super();
    }

    public NotFoundCustomerException(String message) {
        super(message);
    }

}
