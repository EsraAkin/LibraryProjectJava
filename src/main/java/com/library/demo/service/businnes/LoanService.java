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


    public Page<LoanResponse> getAllLoansOfAuthenticatedUser(int page, int size, String sort, String type, Authentication authentication) {

        User user = methodHelper.getAuthenticatedUser(authentication);

        Sort.Direction direction = type.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Loan> loans = loanRepository.findByUserId(user.getId(), pageable);

        boolean isPrivileged = methodHelper.hasAnyRole(user, "ADMIN", "EMPLOYEE");

        return loans.map(loan -> loanMappers.mapLoanToLoanResponse(loan, isPrivileged));

    }

    public LoanResponse getLoanOfAuthenticatedUser(Long loanId, Authentication authentication) {

        // 1. Loan var mı kontrolü
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.LOAN_NOT_FOUND));

        // 2. Giriş yapan kullanıcıyı getir
        User currentUser = methodHelper.loadByEmail(
                ((UserDetailsImpl) authentication.getPrincipal()).getEmail()
        );

        // 3. Sahiplik veya rol kontrolü (ya kendisi olmalı ya da admin/employee)
        if (!loan.getUser().getId().equals(currentUser.getId()) &&
                !methodHelper.hasAnyRole(currentUser, "ADMIN", "EMPLOYEE")) {
            throw new SecurityException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // 4. Notes alanı gösterilmeli mi?
        boolean isPrivileged = methodHelper.hasAnyRole(currentUser, "ADMIN", "EMPLOYEE");

        // 5. Response dön
        return loanMappers.mapLoanToLoanResponse(loan, isPrivileged);
    }

    public Page<LoanResponse> getLoansByUserId(Long userId, int page, int size, String sort, String type) {

        // Kullanıcının var olup olmadığını kontrol et
        User user = methodHelper.getUser(userId);

        // Sıralama yönünü belirle
        Sort.Direction direction = type.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        // Loan'ları getir
        Page<Loan> loans = loanRepository.findByUserId(userId, pageable);

        // Mapper ile dönüştür
        return loans.map(loan -> loanMappers.mapLoanToLoanResponse(loan, true)); // Admin kontrolü gerekmez, true sabit verilir
    }

    public Page<LoanResponse> getAllLoansOfBook(Long bookId, int page, int size, String sort, String type) {

        // Sıralama tipi belirleniyor
        Sort.Direction direction = type.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        // Kitabın ödünç kayıtları alınıyor
        Page<Loan> loans = loanRepository.findByBookId(bookId, pageable);

        // Her loan için LoanResponse'a mapleniyor (user bilgisi ile birlikte)
        return loans.map(loan -> loanMappers.mapLoanToLoanResponse(loan, true)); // `true` => user görünsün

    }

    public LoanResponse getLoanDetailsForAdmin(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.LOAN_NOT_FOUND));

        // Admin/Employee olduğu için detaylar tam dönüyor
        return loanMappers.mapLoanToLoanResponse(loan, true);
    }

    public LoanResponse createLoan(LoanRequest request, Authentication authentication) {

        // 1. Giriş yapan kullanıcıyı getir
        User currentUser = methodHelper.loadByEmail(
                ((UserDetailsImpl) authentication.getPrincipal()).getEmail()
        );

        // 2. Hedef kullanıcıyı getir
        User user = methodHelper.getUser(request.getUserId());

        // 3. Yetki kontrolü
        methodHelper.validateUserUpdatePermission(currentUser, user);

        // 4. Kitabı getir
        Book book = methodHelper.getBook(request.getBookId());

        // 5. İş kurallarını kontrol et
        loanHelper.checkIfBookIsLoanable(book);
        loanHelper.checkIfUserHasOverdueLoans(user);
        loanHelper.checkUserLoanLimit(user);

        // 6. Süre hesapla
        LocalDateTime loanDate = LocalDateTime.now();
        LocalDateTime expireDate = loanHelper.calculateExpireDate(user);

        // 7. Loan objesini mapper ile oluştur
        Loan loan = loanMappers.mapLoanRequestToLoan(request, user, book, loanDate, expireDate);

        // 8. Kitap artık ödünç alınamaz
        book.setLoanable(false);

        // 9. Loan kaydet
        Loan savedLoan = loanRepository.save(loan);

        // 10. Yetki bilgisi
        boolean isPrivileged = methodHelper.hasAnyRole(currentUser, "ADMIN", "STAFF");

        // 11. Response dön
        return loanMappers.mapLoanToLoanResponse(savedLoan, isPrivileged);
    }

    public LoanResponse updateLoan(Long id, @Valid LoanUpdateRequest loanUpdateRequest, Authentication authentication) {

        // 1. Loan nesnesini getir
        Loan loan = loanHelper.getLoanById(id);

        // 2. returnDate null değilse kitap iade ediliyor demektir
        if (loanUpdateRequest.getReturnDate() != null) {

            // Aynı kitap daha önce iade edilmişse işlem yapılamaz
            if (loan.isReturned()) {
                throw new ConflictException(ErrorMessages.LOAN_ALREADY_RETURNED);
            }

            // Kitap iade ediliyor
            loan.setReturnDate(loanUpdateRequest.getReturnDate().atStartOfDay());

            loan.setReturned(true);
            loan.getBook().setLoanable(true);

            // Skor güncelleme
            boolean onTime = !loan.getExpireDate()
                    .toLocalDate()
                    .isBefore(loan.getReturnDate().toLocalDate());

            int currentScore = loan.getUser().getScore();
            loan.getUser().setScore(onTime ? currentScore + 1 : currentScore - 1);

        } else {
            // Sadece expireDate ve notes güncelleniyor
            loan.setExpireDate(loanUpdateRequest.getExpireDate().atStartOfDay());

        }

        // Notlar her iki durumda da güncellenebilir
        loan.setNotes(loanUpdateRequest.getNotes());

        // Veritabanına kaydet
        loanRepository.save(loan);

        // DTO'ya çevirip dön
        return loanMappers.mapLoanToLoanResponse(loan);

    }


    public Page<LoanResponse> getAuthenticatedUserLoans(int page, int size, String sort, String type, Authentication authentication) {

        // 1. Kullanıcıyı al
        User user = methodHelper.getAuthenticatedUser(authentication);

        // 2. Pageable oluştur
        Sort.Direction direction = type.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        // 3. Kullanıcının loan'larını getir
        Page<Loan> userLoans = loanRepository.findByUserId(user.getId(), pageable);

        // 4. DTO'ya map et
        return userLoans.map(loanMappers::mapLoanToLoanResponse);

    }
}

