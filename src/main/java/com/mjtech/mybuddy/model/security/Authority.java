package com.mjtech.mybuddy.model.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * The Authority class implements a authority
 * entity.
 */

@SuppressWarnings("serial")
@RequiredArgsConstructor
@lombok.Generated
public class Authority implements GrantedAuthority {
  private final String authority;

  @Override
  public String getAuthority() {
    return authority;
  }

}
