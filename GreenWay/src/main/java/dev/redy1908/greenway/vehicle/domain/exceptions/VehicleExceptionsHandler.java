package dev.redy1908.greenway.vehicle.domain.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.redy1908.greenway.app.web.models.ErrorResponseDTO;
import dev.redy1908.greenway.vehicle.domain.exceptions.models.VehicleCapacityExceeded;
import dev.redy1908.greenway.vehicle.domain.exceptions.models.VehicleNotFoundException;

@ControllerAdvice
public class VehicleExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(VehicleCapacityExceeded.class)
    public ResponseEntity<ErrorResponseDTO> handleVehicleCapacityExceededException(VehicleCapacityExceeded exception,
            WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.NOT_ACCEPTABLE.value(),
                HttpStatus.NOT_ACCEPTABLE,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomerAlreadyExistsException(VehicleNotFoundException exception,
            WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

}
