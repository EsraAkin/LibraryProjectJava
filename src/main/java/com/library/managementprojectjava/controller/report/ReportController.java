package com.library.managementprojectjava.controller.report;

import com.library.managementprojectjava.payload.response.report.PopularBookResponse;
import com.library.managementprojectjava.payload.response.report.ReportResponse;
import com.library.managementprojectjava.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ReportResponse> getDashboard() {
        return ResponseEntity.ok(reportService.getDashboard());
    }

    @GetMapping("/most-popular-books")
    @PreAuthorize("hasAnyAuthority('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<Page<PopularBookResponse>> getMostPopularBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<PopularBookResponse> response = reportService.getMostPopularBooks(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/unreturned-books")
    @PreAuthorize("hasAnyAuthority('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<Page<PopularBookResponse>> getUnreturnedBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "expireDate") String sort,
            @RequestParam(defaultValue = "desc") String type
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                type.equalsIgnoreCase("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending()
        );

        Page<PopularBookResponse> response = reportService.getUnreturnedBooks(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expired-books")
    @PreAuthorize("hasAnyAuthority('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<Page<PopularBookResponse>> getExpiredBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "expireDate") String sort,
            @RequestParam(defaultValue = "desc") String type
    ) {
        Pageable pageable = PageRequest.of(page, size,
                type.equalsIgnoreCase("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending());

        Page<PopularBookResponse> response = reportService.getExpiredBooks(pageable);

        return ResponseEntity.ok(response);
    }




}
