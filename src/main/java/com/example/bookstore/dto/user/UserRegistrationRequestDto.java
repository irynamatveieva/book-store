package com.example.bookstore.dto.user;

import com.example.bookstore.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldMatch(firstField = "password", secondField = "repeatPassword")
public class UserRegistrationRequestDto {
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
    private String repeatPassword;
    @NotBlank
    @Size(max = 50)
    private String firstName;
    @NotBlank
    @Size(max = 50)
    private String lastName;
    private String shippingAddress;
}
