package de.uko.takeaway.config.api.util;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test/exception")
public class ExceptionHandlingTestController {

    private final ExceptionHandlingTestComponent component;

    @GetMapping
    public void test() throws Exception {
        component.test();
    }

    @PutMapping
    public void testWithPayload(@Valid final ExceptionHandlingTestDto dto) {
        component.test();
    }

}
