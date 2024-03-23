package dev.redy1908.greenway.app.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String statusMsg;
}
