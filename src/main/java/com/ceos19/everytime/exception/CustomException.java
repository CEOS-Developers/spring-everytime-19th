package com.ceos19.everytime.exception;

import org.springframework.http.HttpStatus;

public interface ApplicationException {

    String name();
    HttpStatus getHttpStatus();
    String getMessage();

}
