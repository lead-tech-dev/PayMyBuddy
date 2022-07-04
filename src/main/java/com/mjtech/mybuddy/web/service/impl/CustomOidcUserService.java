package com.mjtech.mybuddy.web.service.impl;


import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.model.security.Role;
import com.mjtech.mybuddy.model.security.UserRole;
import com.mjtech.mybuddy.utility.SecurityUtility;
import com.mjtech.mybuddy.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


/**
 * CustomOidcUserService. class that implement
 * OidcUserService business logic
 */
@Service
public class CustomOidcUserService extends OidcUserService {
  @Autowired
  private UserService userService;

  /**
   * {@inheritDoc}
   */
  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);

    try {
      return processOidcUser(userRequest, oidcUser);
    } catch (Exception ex) {
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }


  /**
   * processOidcUser. Method that save oidc User in database
   *
   * @param userRequest a oidc user request
   * @param oidcUser    oidc user
   * @return oidc user
   * @throws Exception an exception
   */
  private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser)
          throws Exception {

    Map<String, Object> attributes = oidcUser.getAttributes();
    String email = attributes.get("email").toString();
    String name = attributes.get("name").toString();

    Optional<Users> userOptional =
            Optional.ofNullable(userService.findByEmail(email));

    if (!userOptional.isPresent()) {
      Users user = new Users();
      user.setEmail(email);
      user.setUsername(name);
      user.setName(name);
      user.setPassword(SecurityUtility.passwordEncoder().encode("123"));

      Role role = new Role();
      role.setRoleId(1);
      role.setName("ROLE_USER");
      Set<UserRole> userRoles = new HashSet<>();
      userRoles.add(new UserRole(user, role));

      userService.createUser(user, userRoles);

    }


    return oidcUser;
  }
}

