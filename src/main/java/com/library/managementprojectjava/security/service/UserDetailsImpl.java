package com.library.managementprojectjava.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.managementprojectjava.entity.user.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

  private Long id;
  private String email;

  @JsonIgnore
  private String password;

  private List<GrantedAuthority> authorities;

  // Constructor to build UserDetailsImpl from User entity
  public UserDetailsImpl(Long id, String email, String password, Set<Role> roles) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toList());
  }

  // Spring Security uses this method to get the "username", which in our case is email
  @Override
  public String getUsername() {
    return email;
  }

//  // Optional method to directly access email if needed elsewhere
//  public String getEmail() {
//    return email;
//  }

  @Override
  public String getPassword() {
    return password;
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }


  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
