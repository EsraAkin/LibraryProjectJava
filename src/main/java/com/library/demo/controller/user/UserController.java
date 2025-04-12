package com.library.demo.controller.user;

import com.library.demo.payload.request.user.UserRequest;
import com.library.demo.payload.response.businnes.BookResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.payload.response.user.UserResponse;
import com.library.demo.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseMessage<UserResponse> saveUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.saveUser(userRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MEMBER')")
    @PostMapping("/user")
    public ResponseMessage<UserResponse> getAuthenticatedUser(Authentication authentication) {
        return userService.getUserProfile(authentication);

    }


    //TODO
    /*/user/loans
    post /user/loans?page=1&size=10&sort=createDate&type=desc
    It will returnauthenticateduserloans */


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


}
