package com.library.managementprojectjava.payload.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @NotNull(message = "FirstName must not be empty!")
    @Size(min = 2, max = 30, message = "FirstName should be minimum 2 characters")
    private String firstName;

    @NotNull(message = "LastName must not be empty!")
    @Size(min = 2, max = 30, message = "LastName should be minimum 2 characters")
    private String lastName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int score;

    @NotNull(message = "Address must not be empty!")
    @Size(min = 10, max = 100, message = "Adress should be minimum 10 characters")
    private String address;

    @NotNull(message = "Phone number must not be empty!")
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Telefon number must be 999-999-9999 ")
    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "Please enter your email")
    @Email(message = "Please enter valid email")
    @Size(min = 10, max = 80, message = "Your email should be between 10 and 80 chars")
    private String email;

    @NotNull(message = "Please enter your password")
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String resetPasswordCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean builtIn;


}
