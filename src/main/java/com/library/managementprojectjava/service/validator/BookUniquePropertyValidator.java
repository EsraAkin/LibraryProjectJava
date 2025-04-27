package com.library.managementprojectjava.service.validator;

import com.library.managementprojectjava.entity.businnes.Book;
import com.library.managementprojectjava.exception.ConflictException;
import com.library.managementprojectjava.payload.messages.ErrorMessages;
import com.library.managementprojectjava.payload.request.businnes.BookRequest;
import com.library.managementprojectjava.repository.businnes.BookRepository;
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
