package com.library.demo.service.helper;

import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.businnes.Loan;
import com.library.demo.entity.user.User;
import com.library.demo.exception.ConflictException;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.payload.request.businnes.LoanRequest;
import com.library.demo.repository.businnes.LoanRepository;
import com.library.demo.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoanHelper {
    private final LoanRepository loanRepository;
    private final MethodHelper methodHelper;


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


    public void handleBookReturnAndScoreUpdate(Loan loan) {

        // If the book has already been returned an error is thrown
        if (loan.isReturned()) {
            throw new ConflictException(ErrorMessages.LOAN_ALREADY_RETURNED);
        }

        // The book is being returned
        loan.setReturnDate(LocalDateTime.now());
        loan.setReturned(true);

        // Skor updating
        boolean onTime = loan.getReturnDate().isBefore(loan.getExpireDate());
        int currentScore = loan.getUser().getScore();
        loan.getUser().setScore(onTime ? currentScore + 1 : currentScore - 1);

        // The book becomes available for borrowing again
        loan.getBook().setLoanable(true);
    }


    // Updating the score when the book is returned
    public void returnLoan(Long loanId, Authentication authentication) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.LOAN_NOT_FOUND));

        User currentUser = methodHelper.loadByEmail(
                ((UserDetailsImpl) authentication.getPrincipal()).getEmail()
        );

        methodHelper.validateUserUpdatePermission(currentUser, loan.getUser());

        if (loan.isReturned()) {
            throw new ConflictException(ErrorMessages.LOAN_ALREADY_RETURNED);
        }

        // The book is being returned
        loan.setReturnDate(LocalDate.now().atStartOfDay());
        loan.setReturned(true);

        // Skor update
        boolean onTime = loan.getReturnDate().isBefore(loan.getExpireDate());
        int currentScore = loan.getUser().getScore();
        loan.getUser().setScore(onTime ? currentScore + 1 : currentScore - 1);

        // The book becomes available for borrowing again
        loan.getBook().setLoanable(true);

        loanRepository.save(loan);
    }


    //  borrowing books
    public Loan createLoan(LoanRequest request) {
        User user = methodHelper.getUser(request.getUserId());
        Book book = methodHelper.getBook(request.getBookId());

        checkIfBookIsLoanable(book);
        checkIfUserHasOverdueLoans(user);
        checkUserLoanLimit(user);

        LocalDateTime loanDate = LocalDateTime.now();
        LocalDateTime expireDate = calculateExpireDate(user);

        Loan loan = Loan.builder()
                .book(book)
                .user(user)
                .loanDate(loanDate)
                .expireDate(expireDate)
                .notes(request.getNotes())
                .returned(false)
                .build();

        book.setLoanable(false);

        return loanRepository.save(loan);
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.LOAN_NOT_FOUND));
    }





}
