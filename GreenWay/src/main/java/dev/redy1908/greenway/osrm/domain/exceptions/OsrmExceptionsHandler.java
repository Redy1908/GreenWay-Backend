package dev.redy1908.greenway.osrm.domain.exceptions;

import dev.redy1908.greenway.app.web.models.ErrorResponseDTO;
import dev.redy1908.greenway.osrm.domain.exceptions.models.*;
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

    @ExceptionHandler(PointOutOfBoundsException.class)
    public ResponseEntity<ErrorResponseDTO> handlePointOutOfBoundsException(PointOutOfBoundsException exception,
                                                                            WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.NOT_ACCEPTABLE.value(),
                HttpStatus.NOT_ACCEPTABLE,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(CantConnectToOsrmException.class)
    public ResponseEntity<ErrorResponseDTO> handleCantConnectToOsrmException(CantConnectToOsrmException exception,
                                                                            WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.FAILED_DEPENDENCY.value(),
                HttpStatus.FAILED_DEPENDENCY,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.FAILED_DEPENDENCY);
    }


    @ExceptionHandler(OpentopodataDatasetNotConfiguredException.class)
    public ResponseEntity<ErrorResponseDTO> handleOpentopodataDatasetNotConfiguredException(OpentopodataDatasetNotConfiguredException exception,
                                                                                              WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.FAILED_DEPENDENCY.value(),
                HttpStatus.FAILED_DEPENDENCY,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.FAILED_DEPENDENCY);
    }


    @ExceptionHandler(OpentopodataTooManyLocationsException.class)
    public ResponseEntity<ErrorResponseDTO> handleOpentopodataTooManyLocationsException(OpentopodataTooManyLocationsException exception,
                                                                                            WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OpentopodataConnectionRefusedException.class)
    public ResponseEntity<ErrorResponseDTO> handleOpentopodataConnectionRefusedException(OpentopodataConnectionRefusedException exception,
                                                                                            WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.NOT_ACCEPTABLE.value(),
                HttpStatus.NOT_ACCEPTABLE,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_ACCEPTABLE);
    }
}
