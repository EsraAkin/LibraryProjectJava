package com.library.managementprojectjava.service.report;

import com.library.managementprojectjava.payload.response.report.ReportResponse;
import com.library.managementprojectjava.repository.businnes.*;
import com.library.managementprojectjava.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public ReportResponse getDashboard() {
        long books = bookRepository.count();
        long authors = authorRepository.count();
        long publishers = publisherRepository.count();
        long categories = categoryRepository.count();
        long loans = loanRepository.count();
        long unReturnedBooks = loanRepository.countByReturnDateIsNull();
        long expiredBooks = loanRepository.countExpiredBooks();
        long members = userRepository.countMembers();

        return new ReportResponse(books, authors, publishers, categories, loans, unReturnedBooks, expiredBooks, members);
    }
}
