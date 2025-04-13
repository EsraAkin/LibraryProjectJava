package com.library.demo.payload.mappers;

import com.library.demo.entity.businnes.Loan;
import com.library.demo.payload.response.businnes.LoanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanMappers {
    private final BookMappers bookMappers;

    public LoanResponse mapToLoanResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .userId(loan.getUser().getId())
                .bookId(loan.getBook().getId())
                .notes(loan.getNotes())
                .loanDate(loan.getLoanDate().toLocalDate())
                .expireDate(loan.getExpireDate().toLocalDate())
                .book(bookMappers.mapToResponse(loan.getBook()))
                .build();
    }
}
