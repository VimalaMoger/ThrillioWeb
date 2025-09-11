package com.moger.demo.exception;

import com.moger.demo.DTOs.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public String handleBookNotFoundException(BookNotFoundException ex, Model model) {

        model.addAttribute("exception", ex.getClass().getSimpleName());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND);
        return "error";
    }

    @ExceptionHandler
    public String handleDataNotFoundException(DataNotFoundException ex, Model model) {

        model.addAttribute("exception", ex.getClass().getSimpleName());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND);
        return "error";
    }

    @ExceptionHandler
    public String handleDataAlreadyExists(DataAlreadyExistsException ex, Model model) {

        model.addAttribute("exception", ex.getClass().getSimpleName());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", HttpStatus.CONFLICT);
        return "error";
    }

    @ExceptionHandler
    protected String handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Model model) {

        model.addAttribute("exception", ex.getClass().getSimpleName());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", HttpStatus.BAD_REQUEST);
        return "error";
    }

    @ExceptionHandler
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex, WebRequest request, Model model) {

        ErrorResponse result = new ErrorResponse();
        result.setStatus(HttpStatus.BAD_REQUEST.value());
        result.setMessage(ex.getMessage());
        result.setTime(System.currentTimeMillis());

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception ex, Model model) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.CONFLICT.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTime(System.currentTimeMillis());

        if (ex instanceof NullPointerException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
