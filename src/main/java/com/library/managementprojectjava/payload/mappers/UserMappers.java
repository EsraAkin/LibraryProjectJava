package com.library.managementprojectjava.payload.mappers;

import com.library.managementprojectjava.entity.user.Role;
import com.library.managementprojectjava.entity.user.User;
import com.library.managementprojectjava.payload.request.user.UserRequest;
import com.library.managementprojectjava.payload.request.user.UserUpdateRequest;
import com.library.managementprojectjava.payload.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMappers {

    private final PasswordEncoder passwordEncoder;

    public User mapUserRequestToUser(UserRequest userRequest) {
        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .address(userRequest.getAddress())
                .phone(userRequest.getPhone())
                .birthDate(userRequest.getBirthDate())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .builtIn(false)
                .build();
    }


    public UserResponse mapUserToUserResponse(User user) {
        List<String> roleNames = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .phone(user.getPhone())
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .roles(roleNames) // sadece bir kere mapledik
                .build();
    }

    public void updateUserFromUserUpdateRequest(UserUpdateRequest request, User user) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setBirthDate(request.getBirthDate());
    }


}
