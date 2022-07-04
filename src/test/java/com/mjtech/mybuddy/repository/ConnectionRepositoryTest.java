package com.mjtech.mybuddy.repository;

import com.mjtech.mybuddy.enumeration.Status;
import com.mjtech.mybuddy.model.Connection;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.utility.SecurityUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Tag("ConnectionRepositoryTest")
@DisplayName("connectionRepository interface test")
class ConnectionRepositoryTest {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UsersRepository usersRepository;

    private Connection connection;
    private Users sender;
    private Users receiver;

    @BeforeEach
    private void setUp() {
        String encryptedPassword = SecurityUtility.passwordEncoder().encode("123");
        sender = new Users();
        sender.setEmail("cartman@yahoo.fr");
        sender.setUsername("cartman");
        sender.setName("cartman");
        sender.setPassword(encryptedPassword);

        receiver = new Users();
        receiver.setEmail("laplume@yahoo.fr");
        receiver.setUsername("laplume");
        receiver.setName("laplume");
        receiver.setPassword(encryptedPassword);

        connection = new Connection(sender, receiver);
        connection.setStatus(Status.ACTIVE);

        usersRepository.save(sender);
        usersRepository.save(receiver);

        connectionRepository.save(connection);
    }

    @Test
    @DisplayName("findActiveConnection, should return active connection list")
    void findActiveConnection_ShouldReturnActiveConnectionList() {
        // GIVEN
        // WHEN
        Iterable<Connection> results = connectionRepository.findActiveConnection(sender.getId());

        // THEN
        assertThat(results).contains(connection);
    }

    @Test
    @DisplayName("findOneConnection, should return connection for given sender id and receiver id")
    void findOneConnection_ShouldReturnConnection_ForGivenSenderIdAndReceiverId() {
        // GIVEN
        // WHEN
        Optional<Connection> result = connectionRepository.findOneConnection(sender.getId(), receiver.getId());

        // THEN
        assertThat(result.get()).isEqualTo(connection);
    }

    @Test
    @DisplayName("findOneConnection, should return connection for given receiver id and sender id")
    void findOneConnection_ShouldReturnConnection_ForGivenReceiverIdAndSenderId() {
        // GIVEN
        // WHEN
        Optional<Connection> result = connectionRepository.findOneConnection(receiver.getId(), sender.getId());

        // THEN
        assertThat(result.get()).isEqualTo(connection);
    }
}