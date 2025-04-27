package com.library.managementprojectjava.controller.businnes;

import com.library.managementprojectjava.payload.request.businnes.LoanRequest;
import com.library.managementprojectjava.payload.request.businnes.LoanUpdateRequest;
import com.library.managementprojectjava.payload.response.businnes.LoanResponse;
import com.library.managementprojectjava.service.businnes.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MEMBER', 'ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<Page<LoanResponse>> getAllLoansOfAuthenticatedUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "loanDate") String sort,
            @RequestParam(defaultValue = "desc") String type,
            Authentication authentication
    ) {
        Page<LoanResponse> responses = loanService.getAllLoansOfAuthenticatedUser(page, size, sort, type, authentication);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoanById(
            @PathVariable Long id,
            Authentication authentication) {

        LoanResponse loanResponse = loanService.getLoanOfAuthenticatedUser(id, authentication);
        return ResponseEntity.ok(loanResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF', 'ROLE_ADMIN')")
    @GetMapping("/{userId}")
    public Page<LoanResponse> getLoansByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "loanDate") String sort,
            @RequestParam(defaultValue = "desc") String type
    ) {
        return loanService.getLoansByUserId(userId, page, size, sort, type);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF', 'ROLE_ADMIN')")
    @GetMapping("/book/{bookId}")
    public Page<LoanResponse> getAllLoansOfBook(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "loanDate") String sort,
            @RequestParam(defaultValue = "desc") String type
    ) {
        return loanService.getAllLoansOfBook(bookId, page, size, sort, type);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/auth/{loanId}")
    public ResponseEntity<LoanResponse> getLoanDetailsForAdmin(@PathVariable Long loanId) {
        LoanResponse response = loanService.getLoanDetailsForAdmin(loanId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(
            @Valid @RequestBody LoanRequest loanRequest,
            Authentication authentication) {

        LoanResponse response = loanService.createLoan(loanRequest, authentication);
        return ResponseEntity.ok(response);
    }



    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<LoanResponse> updateLoan(
            @PathVariable Long id,
            @RequestBody @Valid LoanUpdateRequest loanUpdateRequest,
            Authentication authentication) {
        LoanResponse response = loanService.updateLoan(id, loanUpdateRequest, authentication);
        return ResponseEntity.ok(response);
    }




}
