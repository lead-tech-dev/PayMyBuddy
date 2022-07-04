package com.mjtech.mybuddy.web.service;

import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.model.security.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * UserService interface structure the business logic
 * of user.
 */
public interface UserService {

  /**
   * getUsers. Method that get user list from database.
   *
   * @return Iterable users
   */
  Iterable<Users> getUsers();

  /**
   * findByUsername. Method that get one user from database.
   *
   * @param username a user username
   * @return user
   */
  Users findByUsername(String username);

  /**
   * findAdminUser. Method that get admin user from database.
   *
   * @return admin user
   */
  Users findAdminUser();

  /**
   * findByEmail. Method that get one user from database.
   *
   * @param email a user email
   * @return user
   */
  Users findByEmail(String email);

  /**
   * findPage. Method that get user page from database.
   *
   * @param pageNumber a requested page
   * @return a user page
   */
  Page<Users> findPage(int pageNumber);

  /**
   * findUserWithoutConnectionRequest. Method that get user list without
   * connection request from database.
   *
   * @param id      a user id
   * @param keyword a search keyword
   * @return Iterable user
   */
  Iterable<Users> findUserWithoutConnectionRequest(long id, String keyword);

  /**
   * createUser. Method that save given user and set userRole in database.
   *
   * @param users      a user
   * @param usersRoles a set of userRole
   * @return a user
   */
  Users createUser(Users users, Set<UserRole> usersRoles) throws Exception;

  /**
   * getUserConnectionsList. Method that get user
   * connection list from database.
   *
   * @param id a user id
   * @return list user
   */
  List<Users> getUserConnectionsList(Long id);

  /**
   * updateUser. Method that update given user
   * in database.
   *
   * @param users a user
   * @return user
   */
  Users updateUser(Users users);

  /**
   * findById. Method that get one user from database.
   *
   * @param id a user id
   * @return optional user
   */
  Optional<Users> findById(long id);

  /**
   * deleteUser. Method that delete given user id in database.
   *
   * @param id a user id
   */
  void deleteUser(long id);

  /**
   * getUsernamePasswordLoginInfo. Method that get one user from database.
   *
   * @param principal an authenticated user
   * @return user
   */
  Users getUserInfo(Principal principal);

  /**
   * getConnectionRequest. Method that get connection user request
   * list from database.
   *
   * @param id an authenticated user id
   * @return user list
   */
  List<Users> getConnectionRequest(@Param("id") Long id);

}
