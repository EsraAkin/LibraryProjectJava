package com.library.managementprojectjava.payload.response.businnes;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.library.managementprojectjava.payload.response.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanResponse {

    private Long id;

    private Long userId;
    private Long bookId;

    private String notes;

    private LocalDate loanDate;

    private LocalDate expireDate;
    private UserResponse user; //

    private BookResponse book; // Kitapla ilgili bilgileri JSON’da dönecek
}
