package com.library.managementprojectjava.controller.report;

import com.library.managementprojectjava.payload.response.report.ReportResponse;
import com.library.managementprojectjava.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
