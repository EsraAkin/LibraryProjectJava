package com.library.demo.service.businnes;

import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.businnes.Loan;
import com.library.demo.entity.user.User;
import com.library.demo.payload.mappers.LoanMappers;
import com.library.demo.payload.messages.SuccessMessages;
import com.library.demo.payload.request.businnes.LoanRequest;
import com.library.demo.payload.response.businnes.LoanResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.repository.businnes.BookRepository;
import com.library.demo.repository.businnes.LoanRepository;
import com.library.demo.security.service.UserDetailsImpl;
import com.library.demo.service.helper.LoanHelper;
import com.library.demo.service.helper.MethodHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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


    public ResponseMessage<LoanResponse> saveLoans(@Valid LoanRequest loanRequest) {

        // 1. Kullanıcıyı ve kitabı al
        User user = methodHelper.getUser(loanRequest.getUserId());
        Book book = methodHelper.getBook(loanRequest.getBookId());

        // 2. İş kurallarını kontrol et (LoanHelper kullanımı)
        loanHelper.checkIfBookIsLoanable(book);
        loanHelper.checkIfUserHasOverdueLoans(user);
        loanHelper.checkUserLoanLimit(user);

        // 3. Tarihleri hesapla
        LocalDateTime loanDate = LocalDateTime.now();
        LocalDateTime expireDate = loanHelper.calculateExpireDate(user);

        // 4. Loan nesnesini oluştur
        Loan loan = loanMappers.mapLoanRequestToLoan(
                loanRequest, user, book, loanDate, expireDate);

        // 5. Kaydet
        Loan savedLoan = loanRepository.save(loan);

        // 6. Kitabı ödünç alınamaz yap
        book.setLoanable(false);
        bookRepository.save(book);

        // 7. Response dön
        return ResponseMessage.<LoanResponse>builder()
                .message(SuccessMessages.LOAN_CREATE)
                .httpStatus(HttpStatus.CREATED)
                .returnBody(loanMappers.mapToLoanResponse(savedLoan))
                .build();
}

    public Page<LoanResponse> getAllLoansOfAuthenticatedUser(int page, int size, String sort, String type, Authentication authentication) {
        User user = methodHelper.loadByEmail(
                ((UserDetailsImpl) authentication.getPrincipal()).getEmail());

        Sort.Direction direction = type.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Loan> loans = loanRepository.findByUserId(user.getId(), pageable);

        boolean isPrivileged = methodHelper.hasAnyRole(user, "ADMIN", "EMPLOYEE");

        return loans.map(loan -> loanMappers.mapToLoanResponse(loan, isPrivileged));

    }
}
