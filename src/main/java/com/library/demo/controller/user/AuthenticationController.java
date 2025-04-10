package com.library.demo.controller.user;


import com.library.demo.payload.request.authentication.LoginRequest;
import com.library.demo.payload.response.authentication.AuthenticationResponse;
import com.library.demo.service.user.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;


  @PostMapping("/signin")
  public ResponseEntity<AuthenticationResponse>authenticate(
      @RequestBody @Valid LoginRequest loginRequest){
    return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
  }

}
