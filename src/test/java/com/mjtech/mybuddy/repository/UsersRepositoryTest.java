package com.mjtech.mybuddy.repository;

import com.mjtech.mybuddy.enumeration.Status;
import com.mjtech.mybuddy.model.Connection;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.model.security.Role;
import com.mjtech.mybuddy.model.security.UserRole;
import com.mjtech.mybuddy.utility.SecurityUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Tag("TransferRepositoryTest")
@DisplayName("transferRepository interface test")
class UsersRepositoryTest {

    private final String encryptedPassword = SecurityUtility.passwordEncoder().encode("123");
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private RoleRepository roleRepository;
    private Users user1;

    private Users user2;

    private Users user3;

    private Users user4;

    @BeforeEach
    private void setUp() {

        user1 = new Users();
        user1.setEmail("eric@yahoo.fr");
        user1.setUsername("eric");
        user1.setName("eric");
        user1.setPassword(encryptedPassword);
        usersRepository.save(user1);


        Role role = new Role();
        role.setRoleId(2);
        role.setName("ROLE_ADMIN");
        role.setUserRoles(Set.of(new UserRole(user1, role)));
        roleRepository.save(role);


        user2 = new Users();
        user2.setEmail("apoline@yahoo.fr");
        user2.setUsername("apoline");
        user2.setName("apoline");
        user2.setPassword(encryptedPassword);
        usersRepository.save(user2);

        user3 = new Users();
        user3.setEmail("martin@yahoo.fr");
        user3.setUsername("martin");
        user3.setName("martin");
        user3.setPassword(encryptedPassword);
        usersRepository.save(user3);

        user4 = new Users();
        user4.setEmail("armand@yahoo.fr");
        user4.setUsername("armand");
        user4.setName("armand");
        user4.setPassword(encryptedPassword);
        usersRepository.save(user4);

        Connection connection1 = new Connection(user1, user2);
        connectionRepository.save(connection1);

        Connection connection2 = new Connection(user3, user4);
        connection2.setStatus(Status.ACTIVE);
        connectionRepository.save(connection2);
    }

    @Test
    @DisplayName("findUserWithoutConnectionRequest, should return user list without connection request")
    void findUserWithoutConnectionRequest_ShouldReturnUserList_ForGivenUserIdAndKeyword() {
        // GIVEN
        // WHEN
        Iterable<Users> results = usersRepository.findUserWithoutConnectionRequest(user1.getId(), "fr");

        // THEN
        assertThat(results).doesNotContain(user2);
    }

    @Test
    @DisplayName("findConnectionRequest, should return connection request list for given id")
    void findConnectionRequest_ShouldReturnConnectionRequestList_ForGivenId() {
        // GIVEN
        // WHEN
        Iterable<Users> results = usersRepository.findConnectionRequest(user2.getId());

        // THEN
        assertThat(results).contains(user1);
    }

    @Test
    @DisplayName("findUserConnections, should return connection list for given id")
    void findUserConnections_ShouldReturnConnectionList_ForGivenId() {
        // GIVEN
        // WHEN
        Iterable<Users> results = usersRepository.findUserConnections(user3.getId());

        // THEN
        assertThat(results).contains(user4);
    }

    @Test
    @DisplayName("findUserAdmin, should return admin")
    void findUserAdmin_ShouldReturnAdminUser() {
        // GIVEN
        // WHEN
        Users result = usersRepository.findUserAdmin();

        // THEN
        assertThat(result).isEqualTo(user1);
    }
}