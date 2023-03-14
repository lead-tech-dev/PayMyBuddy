package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.model.Account;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.model.security.Role;
import com.mjtech.mybuddy.model.security.UserRole;
import com.mjtech.mybuddy.repository.AccountRepository;
import com.mjtech.mybuddy.repository.RoleRepository;
import com.mjtech.mybuddy.repository.UsersRepository;
import com.mjtech.mybuddy.utility.SecurityUtility;
import com.mjtech.mybuddy.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Tag("UserServiceImplTest")
@DisplayName("user service implementation class test")
class UserServiceImplTest {
    private UserService userService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AccountRepository accountRepository;


    private Users user;

    private Users userWithoutConnectionRequest;


    @BeforeEach
    private void setUp() {

        userService = new UserServiceImpl(usersRepository, roleRepository);


        String encryptedPassword = SecurityUtility.passwordEncoder().encode("123");
        user = new Users();
        user.setId(1L);
        user.setEmail("cartman@yahoo.fr");
        user.setUsername("cartman");
        user.setName("cartman");
        user.setPassword(encryptedPassword);

        userWithoutConnectionRequest = new Users();
        userWithoutConnectionRequest.setId(1L);
        userWithoutConnectionRequest.setEmail("laplume@yahoo.fr");
        userWithoutConnectionRequest.setUsername("laplume");
        userWithoutConnectionRequest.setName("laplume");
        userWithoutConnectionRequest.setPassword(encryptedPassword);
    }

    @Test
    @DisplayName("getUsers should return users list")
    void getUsers_ShouldReturnUserList() {
        // GIVEN
        when(usersRepository.findAll()).thenReturn(List.of(user));

        // WHEN
        Iterable<Users> results = userService.getUsers();

        // THEN
        verify(usersRepository, times(1)).findAll();
        assertThat(results).contains(user);
    }

    @Test
    @DisplayName("findByUsername, should return user for given username")
    void findByUsername_ShouldReturnUser_ForGivenUsername() {
        // GIVEN
        when(usersRepository.findByUsername(user.getUsername())).thenReturn(user);

        // WHEN
        Users result = userService.findByUsername(user.getUsername());

        // THEN
        verify(usersRepository, times(1)).findByUsername(user.getUsername());
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("findAdminUser, should return admin")
    void findAdminUser_ShouldReturnAdmin() {
        // GIVEN
        when(usersRepository.findUserAdmin()).thenReturn(user);

        // WHEN
        Users result = userService.findAdminUser();

        // THEN
        verify(usersRepository, times(1)).findUserAdmin();
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("findByUsername, should return user for given email")
    void findByEmail_ShouldReturnUser_ForGivenEmail() {
        // GIVEN
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(user);

        // WHEN
        Users result = userService.findByEmail(user.getEmail());

        // THEN
        verify(usersRepository, times(1)).findByEmail(user.getEmail());
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("findPage, should return user page list for given page number")
    void findPage_ShouldReturnUserPageList_ForGivenPageNumber() {
        // GIVEN
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        Page<Users> users = new PageImpl(List.of(user));
        when(usersRepository.findAll(pageable)).thenReturn(users);

        // WHEN
        Page<Users> results = userService.findPage(pageNumber);

        // THEN
        verify(usersRepository, times(1)).findAll(pageable);
        assertThat(results).contains(user);
    }

    @Test
    @DisplayName("findUserWithoutConnectionRequest, should return user list without connection request")
    void findUserWithoutConnectionRequest_ShouldReturnUserList_ForGivenUserIdAndKeyword() {
        // GIVEN
        when(usersRepository.findUserWithoutConnectionRequest(user.getId(), "laplume"))
                .thenReturn(List.of(userWithoutConnectionRequest));

        // WHEN
        Iterable<Users> users = userService.findUserWithoutConnectionRequest(user.getId(), "laplume");

        // THEN
        verify(usersRepository, times(1)).findUserWithoutConnectionRequest(user.getId(), "laplume");
        assertThat(users).contains(userWithoutConnectionRequest);
    }

    @Test
    @DisplayName("createUser, should return saved user for given user and role list")
    void createUser_ShouldReturnSavedUser_ForGivenUserAndRoleList() throws Exception {
        // GIVEN
        //when(usersRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(usersRepository.save(user)).thenReturn(user);

        // WHEN
        Users result = userService.createUser(user, Set.of(
                new UserRole(user, new Role(1, "ROLE_USER", Set.of()))));

        // THEN
        verify(usersRepository, times(1)).save(user);
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("updateUser, should return updated user for given user")
    void updateUser_ShouldReturnUpdatedUser_ForGivenUser() {
        // GIVEN
        when(usersRepository.save(user)).thenReturn(user);

        // WHEN
        Users result = userService.updateUser(user);

        // THEN
        verify(usersRepository, times(1)).save(user);
        assertThat(result).isEqualTo(user);

    }

    @Test
    @DisplayName("findById, should return user for given user id")
    void findById_ShouldReturnUser_ForGivenUserId() {
        // GIVEN
        when(usersRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        // WHEN
        Optional<Users> result = userService.findById(user.getId());

        // THEN
        verify(usersRepository, times(1)).findById(user.getId());
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("deleteUser, should delete user for given user id")
    void deleteUser_ShouldDeleteUser_ForGivenUserId() {
        // GIVEN
        doNothing().when(usersRepository).deleteById(user.getId());

        // WHEN
        userService.deleteUser(user.getId());

        // THEN
        verify(usersRepository, times(1)).deleteById(user.getId());
        assertThat(userService.getUsers()).doesNotContain(user);
    }


    @Test
    @DisplayName("getUsernamePasswordLoginInfo, should return login user for given principal")
    void getUsernamePasswordLoginInfo_ShouldReturnLoginUser_ForGivenPrincipal() {
        // GIVEN
        // WHEN
        // THEN
    }

    @Test
    @DisplayName("getConnectionRequest, should return connection request list for given user id")
    void getConnectionRequest_ShouldReturnConnectionRequestList_ForGivenUserId() {
        // GIVEN
        when(usersRepository.findConnectionRequest(user.getId()))
                .thenReturn(List.of(userWithoutConnectionRequest));

        // WHEN
        Iterable<Users> results = userService.getConnectionRequest(user.getId());

        // THEN
        verify(usersRepository, times(1)).findConnectionRequest(user.getId());
        assertThat(results).contains(userWithoutConnectionRequest);
    }

    @Test
    @DisplayName("getUserConnectionsList, should return connection list for given user id")
    void getUserConnectionsList_ShouldReturnConnectionList_ForGivenUserId() {
        // GIVEN
        when(usersRepository.findUserConnections(user.getId()))
                .thenReturn(List.of(userWithoutConnectionRequest));

        // WHEN
        Iterable<Users> results = userService.getUserConnectionsList(user.getId());

        // THEN
        verify(usersRepository, times(1)).findUserConnections(user.getId());
        assertThat(results).contains(userWithoutConnectionRequest);
    }
}