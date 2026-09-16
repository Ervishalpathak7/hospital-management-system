package com.hms.backend.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import tools.jackson.databind.exc.InvalidFormatException; // see version note below
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ProblemDetail> handleNotFound(ResourceNotFoundException ex) {
                ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
                pd.setTitle("resource not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleArgumentVoilationHandler(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
                return ResponseEntity.badRequest().body(errors);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ProblemDetail> handleArgumentTypeMisMatchVoilation(
                        MethodArgumentTypeMismatchException ex) {
                ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid " + ex.getName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ProblemDetail handleUnreadableBody(HttpMessageNotReadableException ex) {

                // Spring wraps Jackson's exception, so unwrap one level and check what it
                // really was
                if (ex.getCause() instanceof InvalidFormatException ife
                                && ife.getTargetType() != null
                                && ife.getTargetType().isEnum()) {

                        // Build the JSON path of the bad field, e.g. "architectureStyle" or
                        // "options.style"
                        String field = ife.getPath().stream()
                                        .map(ref -> ref.getPropertyName() != null
                                                        ? ref.getPropertyName()
                                                        : "[" + ref.getIndex() + "]")
                                        .collect(Collectors.joining("."));

                        List<String> allowed = Arrays.stream(ife.getTargetType().getEnumConstants())
                                        .map(value -> value == null ? "null" : value.toString())
                                        .toList();

                        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid value '%s' for field '%s'. Allowed values: %s"
                                                        .formatted(ife.getValue(), field, allowed));
                        pd.setTitle("Invalid enum value");
                        pd.setProperty("field", field);
                        pd.setProperty("allowedValues", allowed);
                        return pd;
                }

                // Any other unreadable body: broken JSON, wrong type for a number field, etc.
                ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST, "Request body is malformed or has invalid values.");
                pd.setTitle("Malformed request body");
                return pd;
        }

        @ExceptionHandler(Exception.class)
        public ProblemDetail handleUnhandledException(Exception ex) {
                System.out.println("Unhandled Exception : " + ex);
                return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occured");

        }
}