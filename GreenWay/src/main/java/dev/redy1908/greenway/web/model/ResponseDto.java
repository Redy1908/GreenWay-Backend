package dev.redy1908.greenway.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResponseDto {


    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String statusMsg;
}
