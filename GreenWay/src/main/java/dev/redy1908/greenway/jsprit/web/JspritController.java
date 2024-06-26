package dev.redy1908.greenway.jsprit.web;

import dev.redy1908.greenway.jsprit.domain.IJspritService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("api/v1/schedule")
@RequiredArgsConstructor
public class JspritController {

    private final IJspritService jspritService;

    @GetMapping
    public ResponseEntity<String> schedule() {
        return ResponseEntity.ok(jspritService.schedule());
    }
}
