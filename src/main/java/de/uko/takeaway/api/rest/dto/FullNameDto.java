package de.uko.takeaway.api.rest.dto;


import jakarta.validation.constraints.NotBlank;

public record FullNameDto(
    @NotBlank String firstName,
    @NotBlank String lastName
) {

}
