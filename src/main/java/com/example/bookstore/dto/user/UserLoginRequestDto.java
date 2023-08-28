package com.example.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotBlank
    @Size(min = 4, max = 50)
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}
