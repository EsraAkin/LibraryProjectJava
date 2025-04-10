package com.library.demo.service.validator;

import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.user.User;
import com.library.demo.exception.ConflictException;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.payload.request.businnes.BookRequest;
import com.library.demo.payload.request.user.UserRequest;
import com.library.demo.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUniquePropertyValidator {
    private final UserRepository userRepository;


    public void checkUniqueProperty(User user, UserRequest userRequest) {
        String updatedPhone = "";
        String updatedEmail = "";
        boolean isChanged = false;

        if (!user.getPhone().equals(userRequest.getPhone())) {
            updatedPhone = userRequest.getPhone();
            isChanged = true;
        }
        if (!user.getEmail().equals(userRequest.getEmail())) {
            updatedEmail = userRequest.getEmail();
            isChanged = true;
        }
        if (isChanged) {
            checkDublication(updatedEmail,updatedPhone);
        }

    }

    public void checkDublication(String phone, String email) {

        if (userRepository.existsByPhone(phone)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER));
        }

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_EMAIL));
        }

    }


}
