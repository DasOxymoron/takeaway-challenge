package de.uko.takeaway.api.rest.dto;

import de.uko.takeaway.domain.domain.EmployeeUpdateAble;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;


public record EmployeeUpdateDto(
    @NotEmpty String lastName,
    @NotEmpty @Email String email,
    @NotNull List<String> hobbies
) implements EmployeeUpdateAble {

    @Override
    public String getLastName() {
        return lastName();
    }

    @Override
    public String getEmail() {
        return email();
    }

    @Override
    public List<String> getHobbies() {
        return hobbies();
    }
}
