package com.library.managementprojectjava.payload.response.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private long books;
    private long authors;
    private long publishers;
    private long categories;
    private long loans;
    private long unReturnedBooks;
    private long expiredBooks;
    private long members;
}
