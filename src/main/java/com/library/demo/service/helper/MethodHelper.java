package com.library.demo.service.helper;

import com.library.demo.entity.businnes.Book;
import com.library.demo.repository.businnes.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodHelper {
    private final BookRepository bookRepository;

    public void validateBookCanBeDeleted(Book book) {
        boolean onLoan = book.getLoans().stream()
                .anyMatch(loan -> loan.getReturnDate() == null);

        if (onLoan) {
            throw new IllegalStateException("This book is currently on loan and cannot be deleted.");
        }
    }


}
