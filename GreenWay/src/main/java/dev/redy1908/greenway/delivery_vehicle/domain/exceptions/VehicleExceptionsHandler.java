package dev.redy1908.greenway.delivery_vehicle.domain.exceptions;

import dev.redy1908.greenway.app.web.models.ErrorResponseDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models.DeliveryVehicleNotFoundException;
import dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models.NoDeliveryAssignedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class VehicleExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DeliveryVehicleNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleVehicleNotFoundException(DeliveryVehicleNotFoundException exception,
                                                                           WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoDeliveryAssignedException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoDeliveryAssignedException(NoDeliveryAssignedException exception,
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
