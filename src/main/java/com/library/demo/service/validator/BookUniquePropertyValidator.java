package com.library.demo.service.validator;

import com.library.demo.entity.businnes.Book;
import com.library.demo.exception.ConflictException;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.payload.request.businnes.BookRequest;
import com.library.demo.repository.businnes.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookUniquePropertyValidator {

    private final BookRepository bookRepository;


    public void checkUniqueProperty(Book book, BookRequest bookRequest) {
        String updatedIsbn = "";
        boolean isChanged = false;

        if (!book.getIsbn().equals(bookRequest.getIsbn())) {
            updatedIsbn = bookRequest.getIsbn();
            isChanged = true;
        }
        if (isChanged) {
            checkDublication(updatedIsbn);
        }

    }

    public void checkDublication(String isbn) {

        if (bookRepository.existsByIsbn(isbn)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_BOOK_FOUND));
        }

    }


}
