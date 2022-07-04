package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserSecurityService. class that implement
 * user security business logic
 */
@Service
public class UserSecurityService implements UserDetailsService {

  @Autowired
  private UsersRepository usersRepository;


  /**
   * loadUserByUsername. Method that get user details from database.
   *
   * @param email an email
   * @return user details
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return usersRepository.findByEmail(email);
  }

}
