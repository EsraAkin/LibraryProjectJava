package com.library.demo.service.user;

import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.user.Role;
import com.library.demo.entity.user.User;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.mappers.UserMappers;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.payload.messages.SuccessMessages;
import com.library.demo.payload.request.user.UserRequest;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.payload.response.user.UserResponse;
import com.library.demo.repository.user.RoleRepository;
import com.library.demo.repository.user.UserRepository;
import com.library.demo.security.service.UserDetailsImpl;
import com.library.demo.service.helper.MethodHelper;
import com.library.demo.service.validator.UserUniquePropertyValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserUniquePropertyValidator userUniquePropertyValidator;
    private final UserMappers userMappers;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MethodHelper methodHelper;


    public ResponseMessage<UserResponse> saveUser(UserRequest userRequest) {
        //validation
        userUniquePropertyValidator.checkDublication(userRequest.getPhone(), userRequest.getEmail());

        // 2. Mapleme: UserRequest -> User entity
        User user = userMappers.mapUserRequestToUser(userRequest);

        // 3. Default rolü ata
        Role defaultRole = roleRepository.findByName("MEMBER")
                .orElseThrow(() -> new RuntimeException("Default rol bulunamadı: MEMBER"));
        user.setRoles(Set.of(defaultRole));


        // 5. Kaydet
        User savedUser = userRepository.save(user);

        // 6. Response döndür
        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATE)
                .returnBody(userMappers.mapUserToUserResponse(savedUser))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public ResponseMessage<UserResponse> getUserProfile(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user=userRepository.findByEmail(userDetails.getEmail()).
                orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE_EMAIL));
        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_FOUND)
                .returnBody(userMappers.mapUserToUserResponse(user))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Page<UserResponse> getPageableUser(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMappers::mapUserToUserResponse);

    }

    public ResponseMessage<UserResponse> getUserById(@Valid Long userId) {
        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_FOUND)
                .returnBody(userMappers.mapUserToUserResponse(methodHelper.getUser(userId)))
                .httpStatus(HttpStatus.OK)
                .build();

    }
}
