package de.uko.takeaway.config.api.util;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionHandlingTestDto {

    @NotEmpty
    private final String message;

}
