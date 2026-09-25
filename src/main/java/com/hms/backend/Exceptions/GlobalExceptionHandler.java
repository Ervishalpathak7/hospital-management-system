package com.hms.backend.Exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
                return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        @ExceptionHandler(InvalidCursorException.class)
        public ProblemDetail handleInvalidCursor(InvalidCursorException ex) {
                ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
                pd.setTitle("Invalid Cursor");
                return pd;
        }

        @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
        public ProblemDetail handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex) {
                ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                                "This doctor was modified by someone else. Reload and try again.");
                pd.setTitle("Concurrent Update");
                return pd;
        }

        @ExceptionHandler(InvalidTimeSlotException.class)
        public ProblemDetail handleInvalidTimeSlotException(InvalidTimeSlotException ex) {
                ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
                return pd;
        }

        @ExceptionHandler(SlotAlreadyBookedException.class)
        public ProblemDetail handleSlotAlreadyBookedExceptionException(SlotAlreadyBookedException ex) {
                ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
                return pd;
        }

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex,
                        HttpHeaders headers,
                        HttpStatusCode status,
                        WebRequest request) {

                Map<String, List<String>> errors = new HashMap<>();
                ex.getFieldErrors().forEach(error -> errors.computeIfAbsent(error.getField(), k -> new ArrayList<>())
                                .add(error.getDefaultMessage()));
                ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, "Validation failed");
                pd.setTitle("Validation errors");
                pd.setProperty("errors", errors);
                return ResponseEntity.status(status).headers(headers).body(pd);
        }

        @Override
        protected ResponseEntity<Object> handleHandlerMethodValidationException(
                        HandlerMethodValidationException ex,
                        HttpHeaders headers,
                        HttpStatusCode status,
                        WebRequest request) {

                Map<String, String> errors = new LinkedHashMap<>();
                ex.getParameterValidationResults().forEach(result -> {
                        String param = result.getMethodParameter().getParameterName();
                        result.getResolvableErrors()
                                        .forEach(err -> errors.put(param, err.getDefaultMessage()));
                });

                ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, "Request validation failed");
                pd.setTitle("Validation Error");
                pd.setProperty("errors", errors);
                return ResponseEntity.status(status).headers(headers).body(pd);
        }

        @ExceptionHandler(Exception.class)
        public ProblemDetail handleUnhandledException(Exception ex) {
                System.out.println("Unhandled Exception : " + ex);
                return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occured");

        }
}