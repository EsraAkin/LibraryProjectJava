package com.library.demo.service.businnes;

import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.businnes.Loan;
import com.library.demo.entity.user.User;
import com.library.demo.exception.ConflictException;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.mappers.LoanMappers;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.payload.request.businnes.LoanRequest;
import com.library.demo.payload.request.businnes.LoanUpdateRequest;
import com.library.demo.payload.response.businnes.LoanResponse;
import com.library.demo.payload.response.businnes.UserLoanResponse;
import com.library.demo.payload.response.user.UserResponse;
import com.library.demo.repository.businnes.BookRepository;
import com.library.demo.repository.businnes.LoanRepository;
import com.library.demo.security.service.UserDetailsImpl;
import com.library.demo.service.helper.LoanHelper;
import com.library.demo.service.helper.MethodHelper;
import com.library.demo.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final MethodHelper methodHelper;
    private final LoanHelper loanHelper;
    private final BookRepository bookRepository;
    private final LoanMappers loanMappers;

    private final UserService userService; // burada UserRepository yerine UserService

    public Page<LoanResponse> getAllLoansOfAuthenticatedUser(int page, int size, String sort, String type, Authentication authentication) {

        User user = methodHelper.getAuthenticatedUser(authentication);

        Sort.Direction direction = type.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Loan> loans = loanRepository.findByUserId(user.getId(), pageable);

        boolean isPrivileged = methodHelper.hasAnyRole(user, "ADMIN", "EMPLOYEE");

        return loans.map(loan -> loanMappers.mapLoanToLoanResponse(loan, isPrivileged));

    }

    public LoanResponse getLoanOfAuthenticatedUser(Long loanId, Authentication authentication) {

        // 1. Check if there is a loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.LOAN_NOT_FOUND));

        // 2. Get the logged in user
        User currentUser = methodHelper.loadByEmail(
                ((UserDetailsImpl) authentication.getPrincipal()).getEmail()
        );

        // 3. Ownership or role control (must be either self or admin/employee)
        if (!loan.getUser().getId().equals(currentUser.getId()) &&
                !methodHelper.hasAnyRole(currentUser, "ADMIN", "EMPLOYEE")) {
            throw new SecurityException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // 4. Should the Notes field be displayed?
        boolean isPrivileged = methodHelper.hasAnyRole(currentUser, "ADMIN", "EMPLOYEE");

        // 5. Return Response
        return loanMappers.mapLoanToLoanResponse(loan, isPrivileged);
    }

    public Page<LoanResponse> getLoansByUserId(Long userId, int page, int size, String sort, String type) {

        // Get the logged in user
        User user = methodHelper.getUser(userId);

        // Sorting direction
        Sort.Direction direction = type.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        // Get the Loan
        Page<Loan> loans = loanRepository.findByUserId(userId, pageable);

        // Mapping
        return loans.map(loan -> loanMappers.mapLoanToLoanResponse(loan, true)); // No admin control required, true constant is given
    }

    public Page<LoanResponse> getAllLoansOfBook(Long bookId, int page, int size, String sort, String type) {

        // Sorting type
        Sort.Direction direction = type.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        // Get book loan records
        Page<Loan> loans = loanRepository.findByBookId(bookId, pageable);

        // Maple to LoanResponse for each loan (with user information)
        return loans.map(loan -> loanMappers.mapLoanToLoanResponse(loan, true)); // `true` => user görünsün

    }

    public LoanResponse getLoanDetailsForAdmin(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.LOAN_NOT_FOUND));

        // Since it is Admin/Employee, the details are complete.
        return loanMappers.mapLoanToLoanResponse(loan, true);
    }

    public LoanResponse createLoan(LoanRequest request, Authentication authentication) {

        // 1.Get the logged in user
        User currentUser = methodHelper.loadByEmail(
                ((UserDetailsImpl) authentication.getPrincipal()).getEmail()
        );

        // 2. Get target user
        User user = methodHelper.getUser(request.getUserId());

        // 3. Authorization control
        methodHelper.validateUserUpdatePermission(currentUser, user);

        // 4. Get the Book
        Book book = methodHelper.getBook(request.getBookId());

        // 5. Check endpoint rules
        loanHelper.checkIfBookIsLoanable(book);
        loanHelper.checkIfUserHasOverdueLoans(user);
        loanHelper.checkUserLoanLimit(user);

        // 6. Time calculate
        LocalDateTime loanDate = LocalDateTime.now();
        LocalDateTime expireDate = loanHelper.calculateExpireDate(user);

        // 7. Create Loan objesini with mapper
        Loan loan = loanMappers.mapLoanRequestToLoan(request, user, book, loanDate, expireDate);

        // 8. The book can no longer be borrowed
        book.setLoanable(false);

        // 9. Loan save
        Loan savedLoan = loanRepository.save(loan);

        // 10. Authorization information
        boolean isPrivileged = methodHelper.hasAnyRole(currentUser, "ADMIN", "STAFF");

        // 11. Return Response
        return loanMappers.mapLoanToLoanResponse(savedLoan, isPrivileged);
    }

    public LoanResponse updateLoan(Long id, @Valid LoanUpdateRequest loanUpdateRequest, Authentication authentication) {

        // 1. Get Loan object
        Loan loan = loanHelper.getLoanById(id);

        // 2. If returnDate is not null, the book is being returned.
        if (loanUpdateRequest.getReturnDate() != null) {

            // If the same book has been returned before, no action can be taken.
            if (loan.isReturned()) {
                throw new ConflictException(ErrorMessages.LOAN_ALREADY_RETURNED);
            }

            // The book is being returned
            loan.setReturnDate(loanUpdateRequest.getReturnDate().atStartOfDay());

            loan.setReturned(true);
            loan.getBook().setLoanable(true);

            // Skor update
            boolean onTime = !loan.getExpireDate()
                    .toLocalDate()
                    .isBefore(loan.getReturnDate().toLocalDate());

            int currentScore = loan.getUser().getScore();
            loan.getUser().setScore(onTime ? currentScore + 1 : currentScore - 1);

        } else {
            // Only expireDate und notes are updating
            loan.setExpireDate(loanUpdateRequest.getExpireDate().atStartOfDay());

        }

        // Notes can be updated in both cases
        loan.setNotes(loanUpdateRequest.getNotes());

        // Save to DB
        loanRepository.save(loan);

        // map  DTO and return
        return loanMappers.mapLoanToLoanResponse(loan);

    }


    public Page<UserLoanResponse> getAuthenticatedUserLoans(int page, int size, String sort, String type, Authentication authentication) {

        User user = methodHelper.getAuthenticatedUser(authentication);

        // Create Pageable
        Sort.Direction direction = type.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        // Get User's Loans
        Page<Loan> userLoans = loanRepository.findByUserId(user.getId(), pageable);
        System.out.println("Loan bulunma sayısı: " + userLoans.getTotalElements());
        userLoans.forEach(loan -> System.out.println("Loan ID: " + loan.getId()));

        // Map Loans to UserLoanResponse and return
        return userLoans.map(loanMappers::mapLoanToUserLoanResponse);
    }



}

