package com.library.demo.service.helper;

import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.businnes.Loan;
import com.library.demo.entity.user.User;
import com.library.demo.exception.ConflictException;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.repository.businnes.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoanHelper {
    private final LoanRepository loanRepository;

    public void checkIfBookIsLoanable(Book book) {
        if (!book.isLoanable()) {
            throw new ConflictException(ErrorMessages.BOOK_NOT_AVAILABLE_FOR_LOAN);
        }
    }

    public void checkIfUserHasOverdueLoans(User user) {
        List<Loan> overdueLoans = loanRepository.findByUserIdAndReturnedFalse(user.getId()).stream()
                .filter(loan -> loan.getExpireDate().isBefore(LocalDateTime.now()))
                .toList();

        if (!overdueLoans.isEmpty()) {
            throw new ConflictException(ErrorMessages.USER_HAS_OVERDUE_BOOKS);
        }
    }

    public void checkUserLoanLimit(User user) {
        long currentLoans = loanRepository.countByUserIdAndReturnedFalse(user.getId());
        int limit = getLoanLimit(user.getScore());

        if (currentLoans >= limit) {
            throw new ConflictException(ErrorMessages.USER_REACHED_LOAN_LIMIT);
        }
    }

    public LocalDateTime calculateExpireDate(User user) {
        int days = getLoanDuration(user.getScore());
        return LocalDateTime.now().plusDays(days);
    }

    public int getLoanLimit(int score) {
        if (score < 50) return 1;
        if (score < 100) return 2;
        return 3;
    }

    public int getLoanDuration(int score) {
        if (score < 50) return 7;
        if (score < 100) return 14;
        return 21;
    }


}
