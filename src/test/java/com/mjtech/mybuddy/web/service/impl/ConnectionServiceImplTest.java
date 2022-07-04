package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.enumeration.Status;
import com.mjtech.mybuddy.model.Connection;
import com.mjtech.mybuddy.model.Transfer;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.repository.ConnectionRepository;
import com.mjtech.mybuddy.repository.UsersRepository;
import com.mjtech.mybuddy.utility.SecurityUtility;
import com.mjtech.mybuddy.web.service.ConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Tag("ConnectionServiceImplTest")
@DisplayName("connection service implementation class test")
class ConnectionServiceImplTest {

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private UsersRepository usersRepository;

    private ConnectionService connectionService;

    private Connection connection;
    private Users sender;
    private Users receiver;

    @BeforeEach
    private void setUp() {
        connectionService = new ConnectionServiceImpl(connectionRepository, usersRepository);

        String encryptedPassword = SecurityUtility.passwordEncoder().encode("123");
        sender = new Users();
        sender.setId(1L);
        sender.setEmail("cartman@yahoo.fr");
        sender.setUsername("cartman");
        sender.setName("cartman");
        sender.setPassword(encryptedPassword);

        receiver = new Users();
        receiver.setId(2L);
        receiver.setEmail("laplume@yahoo.fr");
        receiver.setUsername("laplume");
        receiver.setName("laplume");
        receiver.setPassword(encryptedPassword);

        connection = new Connection(
                1,
                sender,
                receiver,
                Status.ACTIVE,
                new Date(),
                List.of(new Transfer())
        );
    }

    @Test
    @DisplayName("findOneConnection should return optional connection for given current and connected user")
    void findOneConnection_ShouldReturnConnection_ForGivenCurrentAndConnectedUser() {
        // GIVEN
        when(connectionRepository.findOneConnection(sender.getId(), receiver.getId())).thenReturn(Optional.ofNullable(connection));

        // WHEN
        Optional<Connection> result = connectionService.findOneConnection(sender, receiver);

        // THEN
        verify(connectionRepository, times(1)).findOneConnection(sender.getId(), receiver.getId());
        assertThat(result.get()).isEqualTo(connection);
    }

    @Test
    @DisplayName("saveConnection return saved connection for given connection")
    void saveConnection_ShouldReturnSavedConnection_ForGivenConnection() {
        // GIVEN
        when(connectionRepository.save(connection)).thenReturn(connection);

        // WHEN
        Connection result = connectionService.saveConnection(connection);

        // THEN
        verify(connectionRepository, times(1)).save(connection);
        assertThat(result).isEqualTo(connection);
    }

    @Test
    @DisplayName("deleteConnection delete connection for given connection id")
    void deleteConnection_ShouldDeleteConnection_ForGivenConnectionId() {
        // GIVEN
        doNothing().when(connectionRepository).deleteById(connection.getId());

        // WHEN
        connectionService.deleteConnection(connection.getId());

        // THEN
        verify(connectionRepository, times(1)).deleteById(connection.getId());
        assertTrue(connectionService.findOneConnection(sender, receiver).isEmpty());
    }
}