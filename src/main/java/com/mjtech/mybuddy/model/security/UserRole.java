package com.mjtech.mybuddy.model.security;

import com.mjtech.mybuddy.model.Users;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The UserRole class implements a userRole
 * entity.
 */

@Entity
@Table(name = "user_role")
@lombok.Generated
public class UserRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long userRoleId;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "user_id")
  private Users user;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "role_id")
  private Role role;

  public UserRole() {

  }

  public UserRole(Users user, Role role) {
    this.user = user;
    this.role = role;
  }

  public Long getUserRoleId() {
    return userRoleId;
  }

  public void setUserRoleId(Long userRoleId) {
    this.userRoleId = userRoleId;
  }

  public Users getUser() {
    return user;
  }

  public void setUser(Users user) {
    this.user = user;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

}
