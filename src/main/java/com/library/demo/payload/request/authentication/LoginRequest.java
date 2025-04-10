package com.library.demo.payload.request.authentication;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

  @NotNull(message = "Email must not be empty")
  private String email;

  @NotNull(message = "Password must not be empty")
  private String password;

}
