package com.library.managementprojectjava.security.service;


import com.library.managementprojectjava.entity.user.User;
import com.library.managementprojectjava.service.helper.MethodHelper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


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
