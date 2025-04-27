package com.library.managementprojectjava.service.report;

import com.library.managementprojectjava.payload.response.report.PopularBookResponse;
import com.library.managementprojectjava.payload.response.report.PopularUserResponse;
import com.library.managementprojectjava.payload.response.report.ReportResponse;
import com.library.managementprojectjava.repository.businnes.*;
import com.library.managementprojectjava.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Page<PopularBookResponse> getMostPopularBooks(Pageable pageable) {
        Page<Object[]> popularBooks = loanRepository.findMostPopularBooks(pageable);

        List<PopularBookResponse> responseList = popularBooks
                .stream()
                .map(obj -> new PopularBookResponse(
                        ((Number) obj[0]).longValue(),
                        (String) obj[1],
                        (String) obj[2]
                ))
                .toList();

        return new PageImpl<>(responseList, pageable, popularBooks.getTotalElements());
    }


    public Page<PopularBookResponse> getUnreturnedBooks(Pageable pageable) {
        Page<Object[]> unreturnedBooks = loanRepository.findUnreturnedBooks(pageable);

        List<PopularBookResponse> response = unreturnedBooks
                .stream()
                .map(obj -> new PopularBookResponse(
                        ((Number) obj[0]).longValue(),
                        (String) obj[1],
                        (String) obj[2]
                ))
                .toList();

        return new PageImpl<>(response, pageable, unreturnedBooks.getTotalElements());
    }

    public Page<PopularBookResponse> getExpiredBooks(Pageable pageable) {
        Page<Object[]> expiredBooks = loanRepository.findExpiredBooks(pageable);

        List<PopularBookResponse> responses = expiredBooks
                .stream()
                .map(obj -> new PopularBookResponse(
                        ((Number) obj[0]).longValue(),
                        (String) obj[1],
                        (String) obj[2]
                ))
                .toList();

        return new PageImpl<>(responses, pageable, expiredBooks.getTotalElements());
    }

    public Page<PopularUserResponse> getMostBorrowers(Pageable pageable) {
        Page<Object[]> borrowers = loanRepository.findMostBorrowers(pageable);

        List<PopularUserResponse> responses = borrowers
                .stream()
                .map(obj -> new PopularUserResponse(
                        ((Number) obj[0]).longValue(),
                        (String) obj[1]
                ))
                .toList();

        return new PageImpl<>(responses, pageable, borrowers.getTotalElements());
    }
}
