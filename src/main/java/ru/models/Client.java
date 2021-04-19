package ru.models;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class Client {
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 3, max = 25, message = "Name should be between 3 and 25 characters")
    private String name;

    @NotEmpty
    private String phone;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    private Boolean status;
}
