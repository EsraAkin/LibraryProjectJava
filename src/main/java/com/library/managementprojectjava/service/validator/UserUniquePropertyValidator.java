package com.library.managementprojectjava.service.validator;

import com.library.managementprojectjava.entity.user.User;
import com.library.managementprojectjava.exception.ConflictException;
import com.library.managementprojectjava.payload.messages.ErrorMessages;
import com.library.managementprojectjava.payload.request.user.UserRequest;
import com.library.managementprojectjava.payload.request.user.UserUpdateRequest;
import com.library.managementprojectjava.repository.user.UserRepository;
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
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER, phone));
        }

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_EMAIL, email));
        }

    }

    public void checkUniqueProperty(User user, UserUpdateRequest request) {
        if (!user.getPhone().equals(request.getPhone())) {
            checkDublication(request.getPhone(), null);
        }
    }



}
