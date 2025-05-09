package com.library.managementprojectjava.service.user;


import com.library.managementprojectjava.payload.request.authentication.LoginRequest;
import com.library.managementprojectjava.payload.response.authentication.AuthenticationResponse;

import com.library.managementprojectjava.repository.user.UserRepository;
import com.library.managementprojectjava.security.jwt.JwtUtils;
import com.library.managementprojectjava.security.service.UserDetailsImpl;
import com.library.managementprojectjava.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final JwtUtils jwtUtils;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final MethodHelper methodHelper;



  public AuthenticationResponse authenticate(LoginRequest loginRequest) {

    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();
    //injection of security in service
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password));

    //security authentication
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtUtils.generateToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String userRole = userDetails.getAuthorities().iterator().next().getAuthority();

    return AuthenticationResponse.builder()
        .token(token)
        .role(userRole)
        .email(userDetails.getUsername())
        .build();
  }


}
