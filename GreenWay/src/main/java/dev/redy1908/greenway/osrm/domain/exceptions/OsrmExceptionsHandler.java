package dev.redy1908.greenway.osrm.domain.exceptions;

import dev.redy1908.greenway.app.web.models.ErrorResponseDTO;
import dev.redy1908.greenway.osrm.domain.exceptions.models.InvalidOsrmResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class OsrmExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidOsrmResponseException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidOsrmResponseException(InvalidOsrmResponseException exception,
                                                                               WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}