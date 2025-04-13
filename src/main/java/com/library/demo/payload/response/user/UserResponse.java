package com.library.demo.payload.response.user;

import com.library.demo.entity.businnes.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
