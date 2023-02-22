
package com.spring.mydiv.Controller.notUse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HelloWorldController {
    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, world!!";
    }
}