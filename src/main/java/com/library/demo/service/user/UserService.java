package com.library.demo.service.user;

import com.library.demo.entity.user.Role;
import com.library.demo.entity.user.User;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.mappers.UserMappers;
import com.library.demo.payload.messages.SuccessMessages;
import com.library.demo.payload.request.user.UserRequest;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.payload.response.user.UserResponse;
import com.library.demo.repository.user.RoleRepository;
import com.library.demo.repository.user.UserRepository;
import com.library.demo.service.validator.UserUniquePropertyValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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


    public ResponseMessage<UserResponse> saveUser(@Valid UserRequest userRequest) {
        //validation
        userUniquePropertyValidator.checkDublication(userRequest.getPhone(), userRequest.getEmail());

        // 2. Mapleme: UserRequest -> User entity
        User user = userMappers.mapUserRequestToUser(userRequest);

        // 3. Default rolü ata
        Role defaultRole = roleRepository.findByName("MEMBER")
                .orElseThrow(() -> new RuntimeException("Default rol bulunamadı: MEMBER"));
        user.setRoles(Set.of(defaultRole));

        // 4. Şifreyi encode et
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 5. Kaydet
        User savedUser = userRepository.save(user);

        // 6. Response döndür
        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATE)
                .returnBody(userMappers.mapUserToUserResponse(savedUser))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }
}
