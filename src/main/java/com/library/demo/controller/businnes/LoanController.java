package com.library.demo.controller.businnes;

import com.library.demo.payload.request.businnes.LoanRequest;
import com.library.demo.payload.response.businnes.LoanResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.service.businnes.LoanService;
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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    @PostMapping("/loans")
    public ResponseMessage<LoanResponse> saveLoans(@RequestBody @Valid LoanRequest loanRequest){
        return loanService.saveLoans(loanRequest);

    }

    @GetMapping("/loans")
    @PreAuthorize("hasAnyAuthority('ROLE_MEMBER', 'ROLE_EMPLOYEE', 'ROLE_ADMIN')")
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

    @GetMapping("/loans/{id}")
    public ResponseEntity<LoanResponse> getLoanById(
            @PathVariable Long id,
            Authentication authentication) {

        LoanResponse loanResponse = loanService.getLoanOfAuthenticatedUser(id, authentication);
        return ResponseEntity.ok(loanResponse);
    }



}
