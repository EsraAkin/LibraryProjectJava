package com.library.demo.service.helper;

import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.user.User;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.repository.businnes.BookRepository;
import com.library.demo.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodHelper {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public void validateBookCanBeDeleted(Book book) {
        boolean onLoan = book.getLoans().stream()
                .anyMatch(loan -> loan.getReturnDate() == null);

        if (onLoan) {
            throw new IllegalStateException("This book is currently on loan and cannot be deleted.");
        }
    }


    public User loadByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE_EMAIL));
    }
}
