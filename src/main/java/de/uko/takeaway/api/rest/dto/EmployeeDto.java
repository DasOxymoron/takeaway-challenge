package de.uko.takeaway.api.rest.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record EmployeeDto(
    @NotNull UUID id,
    @Valid @NotNull FullNameDto fullName,
    @NotEmpty String email,
    @NotNull List<String> hobbies,
    @NotNull LocalDate birthday
) {
}
