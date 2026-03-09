package shopping.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import shopping.auth.domain.DuplicateEmailException;

@Slf4j
@RestControllerAdvice
class AuthControllerExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateEmailException.class)
    private ProblemDetail duplicateEmailExceptionHandler(DuplicateEmailException e) {
        log.error("[ DuplicateEmailException ] : ", e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }
}
