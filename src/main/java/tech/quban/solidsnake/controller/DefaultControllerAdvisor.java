package tech.quban.solidsnake.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.quban.solidsnake.exception.AlreadyExistsException;
import tech.quban.solidsnake.exception.NotEnoughException;
import tech.quban.solidsnake.exception.NotFoundException;

@RestControllerAdvice
public class DefaultControllerAdvisor {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<?> handleResourceNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    ResponseEntity<?> handleAlreadyExistsException(AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @ExceptionHandler(NotEnoughException.class)
    ResponseEntity<?> handleNotEnoughException(NotEnoughException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
