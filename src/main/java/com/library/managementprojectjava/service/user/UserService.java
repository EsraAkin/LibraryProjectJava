package com.library.managementprojectjava.service.user;

import com.library.managementprojectjava.entity.user.Role;
import com.library.managementprojectjava.entity.user.User;
import com.library.managementprojectjava.exception.ConflictException;
import com.library.managementprojectjava.exception.ResourceNotFoundException;
import com.library.managementprojectjava.payload.mappers.UserMappers;
import com.library.managementprojectjava.payload.messages.ErrorMessages;
import com.library.managementprojectjava.payload.messages.SuccessMessages;
import com.library.managementprojectjava.payload.request.user.UserRequest;
import com.library.managementprojectjava.payload.request.user.UserSaveRequest;
import com.library.managementprojectjava.payload.request.user.UserUpdateRequest;
import com.library.managementprojectjava.payload.response.businnes.ResponseMessage;
import com.library.managementprojectjava.payload.response.user.UserResponse;
import com.library.managementprojectjava.repository.user.RoleRepository;
import com.library.managementprojectjava.repository.user.UserRepository;
import com.library.managementprojectjava.security.service.UserDetailsImpl;
import com.library.managementprojectjava.service.helper.MethodHelper;
import com.library.managementprojectjava.service.validator.UserUniquePropertyValidator;
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

        // 2. Mapping: UserRequest -> User entity
        User user = userMappers.mapUserRequestToUser(userRequest);

        // 3. Assign default role
        Role defaultRole = roleRepository.findByName("MEMBER")
                .orElseThrow(() -> new RuntimeException("Default rol bulunamadı: MEMBER"));
        user.setRoles(Set.of(defaultRole));


        // 5. Save
        User savedUser = userRepository.save(user);

        // 6. Return Response
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
        // Get current user (admin or staff?)
        User currentUser = methodHelper.loadByEmail(((UserDetailsImpl) authentication.getPrincipal()).getEmail());

        // Get roles with entered role ids
        Set<Role> rolesToAssign = userSaveRequest.getRoleIdList().stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.ROLE_NOT_FOUND) + id)))
                .collect(Collectors.toSet());

        Set<String> roleNames = rolesToAssign.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        // Authority check
        methodHelper.checkStaffCannotAssignAdminOrStaffRoles(currentUser, roleNames);

        // Unique check
        userUniquePropertyValidator.checkDublication(userSaveRequest.getPhone(), userSaveRequest.getEmail());

        // Create user object
        User user = userMappers.mapUserRequestToUser(userSaveRequest);
        user.setRoles(rolesToAssign);

        // Save
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

        // Get the user to update
        User userToUpdate = methodHelper.getUser(userId);

        // Get the logged in user
        User currentUser = methodHelper.loadByEmail(
                ((UserDetailsImpl) authentication.getPrincipal()).getEmail());

        //Check authorization
        methodHelper.validateUserUpdatePermission(currentUser, userToUpdate);

        // Unique check
        userUniquePropertyValidator.checkUniqueProperty(userToUpdate, userUpdateRequest);

        // Set updateable fields
        userMappers.updateUserFromUserUpdateRequest(userUpdateRequest, userToUpdate);

        // Save updated user
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



    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE + email));
    }




}
