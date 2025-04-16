package com.library.demo.payload.request.businnes;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LoanUpdateRequest {

    @FutureOrPresent(message = "Expire date must be today or in the future")
    private LocalDate expireDate;

    private LocalDate returnDate;

    private String notes;

}
