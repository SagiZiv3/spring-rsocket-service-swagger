package com.example.reactiveservice.logic.db.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@Getter
public class BadParameterException extends RuntimeException{
    private static final long serialVersionUID = -4677790877314785097L;
    private final String argName;
    private final Object argValue;

    public BadParameterException(String argName, Object argValue) {
        super(String.format("Invalid parameter value (%s) for parameter: '%s'", argValue, argName));
        this.argName = argName;
        this.argValue = argValue;
    }

    public BadParameterException(String message, String argName, Object argValue) {
        super(message);
        this.argName = argName;
        this.argValue = argValue;
    }
}