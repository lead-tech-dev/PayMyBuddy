package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.model.Connection;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.repository.ConnectionRepository;
import com.mjtech.mybuddy.repository.UsersRepository;
import com.mjtech.mybuddy.web.service.ConnectionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


/**
 * ConnectionServiceImpl. class that implement
 * connection business logic
 */
@Service
@Transactional
public class ConnectionServiceImpl implements ConnectionService {
  private final ConnectionRepository connectionRepository;

  private final UsersRepository usersRepository;

  public ConnectionServiceImpl(ConnectionRepository connectionRepository,
                               UsersRepository usersRepository) {
    this.connectionRepository = connectionRepository;
    this.usersRepository = usersRepository;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Connection> findOneConnection(Users currentUser,
                                                Users acceptedConnectionUser) {
    return connectionRepository.findOneConnection(currentUser.getId(),
            acceptedConnectionUser.getId());
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Connection saveConnection(Connection connection) {
    connectionRepository.save(connection);
    return connection;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteConnection(Long id) {
    connectionRepository.deleteById(id);
  }

}
