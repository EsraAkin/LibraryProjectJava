package com.library.managementprojectjava.payload.response.user;

import com.library.managementprojectjava.entity.businnes.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserResponse {


    private Long id;

    private String firstName;

    private String lastName;

    private int score;

    private String address;

    private String phone;

    private LocalDate birthDate;

    private String email;


    private LocalDateTime createDate;

    private String resetPasswordCode;

    private Boolean builtIn;


    private List<Loan> loans;

    private List<String> roles;

}
