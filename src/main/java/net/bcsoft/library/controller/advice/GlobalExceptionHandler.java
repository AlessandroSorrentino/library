package net.bcsoft.library.controller.advice;

import lombok.extern.log4j.Log4j2;
import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> constraintViolationHandler(ConstraintViolationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        errorMap.put("Error: ", errorMessage);
        log.error("Error: " + errorMessage);
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Map<String, String> notFoundHandler(NotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error:", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Map<String, String> badRequestHandler(BadRequestException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error:", ex.getMessage());
        return errorMap;
    }
}
