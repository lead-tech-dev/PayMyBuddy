package com.mjtech.mybuddy.web.service;

import com.mjtech.mybuddy.model.Connection;
import com.mjtech.mybuddy.model.Users;

import java.util.Optional;

/**
 * ConnectionService interface structure the business logic
 * of connection.
 */

public interface ConnectionService {

  /**
   * findOneConnection. Method that get one connection from database.
   *
   * @param currentUser            an authenticated user
   * @param acceptedConnectionUser a relationship user
   * @return optional connection
   */
  Optional<Connection> findOneConnection(Users currentUser, Users acceptedConnectionUser);

  /**
   * saveConnection. Method that save given connection in database.
   *
   * @param connection a connection
   * @return a connection
   */
  Connection saveConnection(Connection connection);

  /**
   * deleteConnection. Method that delete given connection in database.
   *
   * @param id a connection id
   */
  void deleteConnection(Long id);

}
