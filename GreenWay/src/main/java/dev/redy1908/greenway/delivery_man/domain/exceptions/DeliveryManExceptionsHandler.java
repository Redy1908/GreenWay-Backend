package dev.redy1908.greenway.delivery_man.domain.exceptions;

import dev.redy1908.greenway.app.web.models.ErrorResponseDTO;
import dev.redy1908.greenway.delivery_man.domain.exceptions.models.DeliveryManAlreadyExistsException;
import dev.redy1908.greenway.delivery_man.domain.exceptions.models.DeliveryManNotFoundException;
import dev.redy1908.greenway.delivery_man.domain.exceptions.models.NoFreeDeliveryManFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DeliveryManExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DeliveryManNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleDeliveryManNotFoundException(DeliveryManNotFoundException exception,
                                                                               WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoFreeDeliveryManFound.class)
    public ResponseEntity<ErrorResponseDTO> handleNoFreeDeliveryManFoundException(NoFreeDeliveryManFound exception,
                                                                                  WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.NOT_ACCEPTABLE.value(),
                HttpStatus.NOT_ACCEPTABLE,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DeliveryManAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleDeliveryManAlreadyExistsException(DeliveryManAlreadyExistsException exception,
                                                                               WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.ALREADY_REPORTED.value(),
                HttpStatus.ALREADY_REPORTED,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.ALREADY_REPORTED);
    }
}
