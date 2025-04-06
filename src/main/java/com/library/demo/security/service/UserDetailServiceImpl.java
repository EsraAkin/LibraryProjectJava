package com.library.demo.security.service;


import com.library.demo.entity.user.User;
import com.library.demo.service.helper.MethodHelper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.library.demo.security.service.UserDetailsImpl;


@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

  private final MethodHelper methodHelper;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = methodHelper.loadByEmail(email);
    return new UserDetailsImpl(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            user.getRoles());

  }
}
