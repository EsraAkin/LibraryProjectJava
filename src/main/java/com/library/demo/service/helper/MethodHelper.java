package com.library.demo.service.helper;

import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.user.User;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.repository.businnes.BookRepository;
import com.library.demo.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.Set;

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


    public User getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, id)));
    }

    public void checkStaffCannotAssignAdminOrStaffRoles(User currentUser, Set<String> requestedRoles) {
        boolean isStaff = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("STAFF"));

        if (isStaff && requestedRoles.stream().anyMatch(role ->
                role.equals("ADMIN") || role.equals("STAFF"))) {
            throw new IllegalArgumentException("STAFF users can only assign MEMBER roles.");
        }
    }

    public void validateUserUpdatePermission(User currentUser, User targetUser) {
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        boolean isStaff = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("STAFF"));

        // Admin her kullanıcıyı güncelleyebilir
        if (isAdmin) return;

        // Staff sadece MEMBER kullanıcılarını güncelleyebilir
        if (isStaff && targetUser.getRoles().stream()
                .allMatch(role -> role.getName().equals("MEMBER"))) {
            return;
        }

        // Kullanıcı kendini güncelliyor mu?
        if (currentUser.getId().equals(targetUser.getId())) {
            return;
        }

        // Diğer durumlar: yetki yok
        throw new SecurityException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);

    }

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.BOOK_NOT_FOUND, id)));
    }

    public boolean hasAnyRole(User user, String... roles) {
        return user.getRoles()
                .stream()
                .map(role -> role.getName()) // name: örn. "ADMIN"
                .anyMatch(userRole -> Arrays.asList(roles).contains(userRole));
    }



}
