package com.library.demo.payload.mappers;

import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.businnes.Loan;
import com.library.demo.entity.user.User;
import com.library.demo.payload.request.businnes.LoanRequest;
import com.library.demo.payload.response.businnes.LoanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoanMappers {
    private final BookMappers bookMappers;

    public Loan mapLoanRequestToLoan(LoanRequest request, User user, Book book, LocalDateTime loanDate, LocalDateTime expireDate) {
        return Loan.builder()
                .user(user)
                .book(book)
                .notes(request.getNotes())
                .loanDate(loanDate)
                .expireDate(expireDate)
                .returned(false)
                .build();
    }


    public LoanResponse mapLoanToLoanResponse(Loan loan) {
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
    public LoanResponse mapLoanToLoanResponse(Loan loan, boolean includeNotes) {
        return LoanResponse.builder()
                .id(loan.getId())
                .userId(loan.getUser().getId())
                .bookId(loan.getBook().getId())
                .loanDate(loan.getLoanDate().toLocalDate())
                .expireDate(loan.getExpireDate().toLocalDate())
                .book(bookMappers.mapBookToBookResponse(loan.getBook()))
                .notes(includeNotes ? loan.getNotes() : null)
                .build();
    }


}
