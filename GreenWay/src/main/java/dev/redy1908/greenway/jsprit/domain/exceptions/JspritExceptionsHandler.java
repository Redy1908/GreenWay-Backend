package dev.redy1908.greenway.jsprit.domain.exceptions;

import dev.redy1908.greenway.app.web.models.ErrorResponseDTO;
import dev.redy1908.greenway.jsprit.domain.exceptions.models.NoDeliveryToOrganizeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class JspritExceptionsHandler {

    @ExceptionHandler(NoDeliveryToOrganizeException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoDeliveryToOrganizeException(NoDeliveryToOrganizeException exception,
                                                                                WebRequest webRequest) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.TOO_EARLY.value(),
                HttpStatus.TOO_EARLY,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.TOO_EARLY);
    }

}
