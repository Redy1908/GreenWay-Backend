package dev.redy1908.greenway.vehicle_deposit.domain.exceptions;

import dev.redy1908.greenway.app.web.models.ErrorResponseDTO;
import dev.redy1908.greenway.vehicle_deposit.domain.exceptions.models.VehicleDepositAlreadyExistsException;
import dev.redy1908.greenway.vehicle_deposit.domain.exceptions.models.VehicleDepositNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class VehicleDepositExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(VehicleDepositAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleVehicleDepositAlreadyExistsException(VehicleDepositAlreadyExistsException exception,
                                                                                WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.ALREADY_REPORTED.value(),
                HttpStatus.ALREADY_REPORTED,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(VehicleDepositNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleVehicleDepositNotFoundException(VehicleDepositNotFoundException exception,
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
