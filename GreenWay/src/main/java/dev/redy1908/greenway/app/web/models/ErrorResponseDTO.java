package dev.redy1908.greenway.app.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    private String apiPath;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String errorMsg;
    private LocalDateTime errorTime;
}
