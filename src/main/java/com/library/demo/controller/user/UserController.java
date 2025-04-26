package com.library.demo.controller.user;

import com.library.demo.payload.request.user.UserRequest;
import com.library.demo.payload.request.user.UserSaveRequest;
import com.library.demo.payload.request.user.UserUpdateRequest;
import com.library.demo.payload.response.businnes.LoanResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.payload.response.businnes.UserLoanResponse;
import com.library.demo.payload.response.user.UserResponse;
import com.library.demo.service.businnes.LoanService;
import com.library.demo.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final LoanService loanService;


    @PostMapping("/register")
    public ResponseMessage<UserResponse> saveUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.saveUser(userRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MEMBER')")
    @PostMapping("/user")
    public ResponseMessage<UserResponse> getAuthenticatedUser(Authentication authentication) {
        return userService.getUserProfile(authentication);

    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MEMBER')")
    @GetMapping("/user/loans")
    public ResponseEntity<Page<UserLoanResponse>> getUserLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createDate") String sort,
            @RequestParam(defaultValue = "desc") String type,
            Authentication authentication) {

        Page<UserLoanResponse> response = loanService.getAuthenticatedUserLoans(page, size, sort, type, authentication);
        return ResponseEntity.ok(response);
    }



    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/users")
    public Page<UserResponse> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createDate") String sort,
            @RequestParam(defaultValue = "desc") String type
    ) {
        Sort.Direction direction = type.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return userService.getPageableUser(pageable);
    }


    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseMessage<UserResponse> getUserById(@PathVariable @Valid Long userId) {
        return userService.getUserById(userId);

    }


    @PostMapping("users")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseMessage<UserResponse> userSaveWithRole(@RequestBody @Valid UserSaveRequest userSaveRequest,
                                                          Authentication authentication) {
        return userService.saveUserWithRole(userSaveRequest, authentication);
    }

    @PutMapping("/users/{user_id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MEMBER')")
    public ResponseMessage<UserResponse> updateUserById(@Valid @RequestBody UserUpdateRequest userUpdateRequest,
                                                        @PathVariable("user_id") Long userId,
                                                        Authentication authentication) {
        return userService.updateUserById(userUpdateRequest, userId, authentication);
    }


    @DeleteMapping("/users/{user_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseMessage<UserResponse> deleteUser(@PathVariable Long user_id) {
        return userService.deleteUserById(user_id);
    }




}
