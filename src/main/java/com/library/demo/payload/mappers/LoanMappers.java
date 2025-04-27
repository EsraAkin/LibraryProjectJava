package com.library.demo.payload.mappers;

import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.businnes.Loan;
import com.library.demo.entity.user.User;
import com.library.demo.payload.request.businnes.LoanRequest;
import com.library.demo.payload.response.businnes.LoanResponse;
import com.library.demo.payload.response.businnes.UserLoanResponse;
import lombok.RequiredArgsConstructor;
import com.library.demo.entity.user.Role;
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
    public UserLoanResponse mapLoanToUserLoanResponse(Loan loan) {
        User user = loan.getUser();

        return UserLoanResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .score(user.getScore())
                .address(user.getAddress())
                .phone(user.getPhone())
                .birthDate(user.getBirthDate())
                .createDate(user.getCreateDate())
                .resetPasswordCode(user.getResetPasswordCode())
                .builtIn(user.getBuiltIn())
                .roles(user.getRoles() != null ?
                        user.getRoles().stream()
                                .map(Role::getName) // Role nesnesinden name bilgisini alıyoruz
                                .toList()
                        : null
                )


                // Loan related fields
                .bookId(loan.getBook() != null ? loan.getBook().getId() : null)
                .build();
    }


}
