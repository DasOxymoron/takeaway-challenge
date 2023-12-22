package de.uko.takeaway.api.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record EmployeeCreationDto(
    @NotNull @Valid FullNameDto fullName,
    @NotEmpty  @Email String email,
    @NotNull List<String> hobbies,
    @NotNull LocalDate birthday
) {

}
