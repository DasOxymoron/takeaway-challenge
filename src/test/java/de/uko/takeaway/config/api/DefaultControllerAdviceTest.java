package de.uko.takeaway.config.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import de.uko.takeaway.domain.exception.NotFoundException;
import de.uko.takeaway.config.api.util.ExceptionHandlingTestComponent;
import de.uko.takeaway.config.api.util.ExceptionHandlingTestController;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ExceptionHandlingTestController.class)
class DefaultControllerAdviceTest {

    @MockBean
    private ExceptionHandlingTestComponent component;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void shouldRenderNotFoundException() {
        UUID id = UUID.fromString("a26e1bc2-023f-4f08-bdc5-7a62b0cd81b8");
        doThrow(new NotFoundException("test", id)).when(component).test();

        mockMvc.perform(MockMvcRequestBuilders.get("/test/exception"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(jsonPath("$.title").value("Not Found"))
            .andExpect(jsonPath("$.detail").value("test with id a26e1bc2-023f-4f08-bdc5-7a62b0cd81b8 not found"));
    }

    @Test
    @SneakyThrows
    void shouldRenderIllegalArgException() {
        doThrow(new IllegalArgumentException("test")).when(component).test();

        mockMvc.perform(MockMvcRequestBuilders.get("/test/exception"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Bad Request"))
            .andExpect(jsonPath("$.detail").value("test"));
    }

    @Test
    @SneakyThrows
    void shouldRenderBeanValidationProblems() {
        mockMvc.perform(MockMvcRequestBuilders.put("/test/exception")
                .contentType("application/json")
                .content("{}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Constraint Violation"))
            .andExpect(jsonPath("$.violations[0].field").value("message"))
            .andExpect(jsonPath("$.violations[0].message").value("must not be empty"))
            .andExpect(jsonPath("$.type").value("https://www.lieferando.de/problem/constraint-violation"));
    }
}