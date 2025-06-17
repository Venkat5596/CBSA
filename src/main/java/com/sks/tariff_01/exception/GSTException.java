package com.sks.tariff_01.exception;


import com.sks.tariff_01.exception.custom.GSTNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GSTException {

    @ExceptionHandler(GSTNotFound.class)
    public ResponseEntity<String> GSTNotFound(Exception e) {
return ResponseEntity.badRequest().body(e.getMessage());
//return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
