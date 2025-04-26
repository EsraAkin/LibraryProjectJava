package com.library.demo.payload.response.businnes;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.user.User;
import com.library.demo.payload.response.user.UserResponse;
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
