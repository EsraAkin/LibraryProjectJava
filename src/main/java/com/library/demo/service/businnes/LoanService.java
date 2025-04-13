package com.library.demo.service.businnes;

import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.businnes.Loan;
import com.library.demo.entity.user.User;
import com.library.demo.payload.mappers.LoanMappers;
import com.library.demo.payload.request.businnes.LoanRequest;
import com.library.demo.payload.response.businnes.LoanResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.repository.businnes.BookRepository;
import com.library.demo.repository.businnes.LoanRepository;
import com.library.demo.service.helper.LoanHelper;
import com.library.demo.service.helper.MethodHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .notes(loanRequest.getNotes())
                .loanDate(loanDate)
                .expireDate(expireDate)
                .returned(false)
                .build();

        // 5. Kaydet
        Loan savedLoan = loanRepository.save(loan);

        // 6. Kitabı ödünç alınamaz yap
        book.setLoanable(false);
        bookRepository.save(book);

        // 7. Response dön
        return ResponseMessage.<LoanResponse>builder()
                .message("Loan has been created successfully.")
                .httpStatus(HttpStatus.CREATED)
                .returnBody(loanMappers.mapToLoanResponse(savedLoan))
                .build();
}
}
