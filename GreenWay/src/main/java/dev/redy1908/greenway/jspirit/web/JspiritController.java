package dev.redy1908.greenway.jspirit.web;

import dev.redy1908.greenway.jspirit.domain.IJspritService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/jspirit")
@RequiredArgsConstructor
public class JspiritController {

    private final IJspritService jspritService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void schedule() {
        jspritService.schedule();
    }
}
