package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.enumeration.Status;
import com.mjtech.mybuddy.model.Account;
import com.mjtech.mybuddy.model.Connection;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.model.security.UserRole;
import com.mjtech.mybuddy.repository.RoleRepository;
import com.mjtech.mybuddy.repository.UsersRepository;
import com.mjtech.mybuddy.web.service.AccountService;
import com.mjtech.mybuddy.web.service.ConnectionService;
import com.mjtech.mybuddy.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * UserServiceImpl. class that implement
 * user business logic
 */
@Transactional
@Service
@Slf4j
public class UserServiceImpl implements UserService {
  private final UsersRepository usersRepository;

  private final RoleRepository roleRepository;


  @Autowired
  private  OAuth2AuthorizedClientService auth2AuthorizedClientService;

  @Autowired
  private ConnectionService connectionService;

  @Autowired
  private AccountService accountService;

  public UserServiceImpl(UsersRepository usersRepository, RoleRepository roleRepository ){
    this.usersRepository = usersRepository;
    this.roleRepository = roleRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Users> getUsers() {
    return usersRepository.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Users findByUsername(String username) {
    return usersRepository.findByUsername(username);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Users findAdminUser() {
    return usersRepository.findUserAdmin();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Users findByEmail(String email) {
    return usersRepository.findByEmail(email);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<Users> findPage(int pageNumber) {
    Pageable pageable = PageRequest.of(pageNumber - 1, 5);
    return usersRepository.findAll(pageable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Users> findUserWithoutConnectionRequest(long id, String keyword) {
    return usersRepository.findUserWithoutConnectionRequest(id, keyword);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Users createUser(Users users, Set<UserRole> usersRoles) throws Exception {
    Users localUsers = usersRepository.findByUsername(users.getUsername());
    Users admin = usersRepository.findUserAdmin();
    if (localUsers != null) {
      log.info("Users {} already exists. Nothing will be done", users.getUsername());
    } else {
      for (UserRole ur : usersRoles) {
        System.out.println(ur.getRole());
        roleRepository.save(ur.getRole());
      }

      users.getUserRoles().addAll(usersRoles);

      localUsers = usersRepository.save(users);

    }

    return localUsers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Users updateUser(Users user) {
    usersRepository.save(user);
    return user;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Users> findById(long id) {
    return usersRepository.findById(id);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteUser(long id) {
    usersRepository.deleteById(id);
  }


  /**
   * {@inheritDoc}
   */

  @Override
  public Users getUserInfo(Principal principal) {
    Users user = null;

    if (principal instanceof UsernamePasswordAuthenticationToken) {
      user = getUsernamePasswordLoginInfo(principal);
    } else {
      user = getOAuth2LoginInfo(principal);
    }
    return user;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<Users> getConnectionRequest(Long id) {
    return usersRepository.findConnectionRequest(id);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<Users> getUserConnectionsList(Long id) {
    return usersRepository.findUserConnections(id);
  }

  private Users getOAuth2LoginInfo(Principal user) {
    OAuth2User principal = ((OAuth2AuthenticationToken) user).getPrincipal();

    return usersRepository.findByEmail(principal.getAttribute("email"));
  }

  private Users getUsernamePasswordLoginInfo(Principal principal) {
    StringBuffer usernameInfo = new StringBuffer();
    UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) principal);
    Users users = null;
    if (token.isAuthenticated()) {
      System.out.println(token.getPrincipal());
      users = (Users) token.getPrincipal();
    } else {
      usernameInfo.append("NA");
    }
    return users;
  }

}
