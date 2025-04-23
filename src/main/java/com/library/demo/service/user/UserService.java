package com.library.demo.service.user;

import com.library.demo.entity.user.Role;
import com.library.demo.entity.user.User;
import com.library.demo.exception.ConflictException;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.mappers.UserMappers;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.payload.messages.SuccessMessages;
import com.library.demo.payload.request.user.UserRequest;
import com.library.demo.payload.request.user.UserSaveRequest;
import com.library.demo.payload.request.user.UserUpdateRequest;
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
import java.util.stream.Collectors;

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
        User user = userRepository.findByEmail(userDetails.getEmail()).
                orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE_EMAIL));
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

    public ResponseMessage<UserResponse> saveUserWithRole(@Valid UserSaveRequest userSaveRequest,
                                                          Authentication authentication) {
        // Mevcut kullanıcıyı al (admin mi staff mı?)
        User currentUser = methodHelper.loadByEmail(((UserDetailsImpl) authentication.getPrincipal()).getEmail());

        // Girilen role id’leri ile roller alınıyor
        Set<Role> rolesToAssign = userSaveRequest.getRoleIdList().stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.ROLE_NOT_FOUND) + id)))
                .collect(Collectors.toSet());

        Set<String> roleNames = rolesToAssign.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        // Yetki kontrolü
        methodHelper.checkStaffCannotAssignAdminOrStaffRoles(currentUser, roleNames);

        // Benzersizlik kontrolü
        userUniquePropertyValidator.checkDublication(userSaveRequest.getPhone(), userSaveRequest.getEmail());

        // Kullanıcı nesnesi oluştur
        User user = userMappers.mapUserRequestToUser(userSaveRequest);
        user.setRoles(rolesToAssign);

        // Kaydet
        User savedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATE)
                .returnBody(userMappers.mapUserToUserResponse(savedUser))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }


    public ResponseMessage<UserResponse> updateUserById(@Valid UserUpdateRequest userUpdateRequest,
                                                        Long userId,
                                                        Authentication authentication) {

        // Güncellenecek kullanıcıyı getir
        User userToUpdate = methodHelper.getUser(userId);

        // Giriş yapan kullanıcıyı getir
        User currentUser = methodHelper.loadByEmail(
                ((UserDetailsImpl) authentication.getPrincipal()).getEmail());

        // Yetki kontrolü yap
        methodHelper.validateUserUpdatePermission(currentUser, userToUpdate);

        // Benzersizlik kontrolü
        userUniquePropertyValidator.checkUniqueProperty(userToUpdate, userUpdateRequest);

        // Güncellenebilir alanları set et
        userMappers.updateUserFromUserUpdateRequest(userUpdateRequest, userToUpdate);

        // Güncellenmiş kullanıcıyı kaydet
        User updatedUser = userRepository.save(userToUpdate);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_UPDATE)
                .httpStatus(HttpStatus.OK)
                .returnBody(userMappers.mapUserToUserResponse(updatedUser))
                .build();
    }

    public ResponseMessage<UserResponse> deleteUserById(Long user_id) {
        User user = methodHelper.getUser(user_id);

        if (user.getLoans() != null && !user.getLoans().isEmpty()) {
            throw new ConflictException(ErrorMessages.USER_HAS_LOANS);
        }

        userRepository.delete(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_DELETE)
                .httpStatus(HttpStatus.OK)
                .returnBody(userMappers.mapUserToUserResponse(user))
                .build();
    }
}
