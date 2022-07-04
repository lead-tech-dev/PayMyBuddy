package com.mjtech.mybuddy.repository;

import com.mjtech.mybuddy.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * UsersRepository. Interface that allows to access and
 * persist data between Users object and buddy database
 */
@Repository
public interface UsersRepository extends PagingAndSortingRepository<Users, Long> {
  Users findByUsername(String username);

  Users findByEmail(String email);

  @Query(value = "select * from users u where id not in\n" +
          "((SELECT receiver_id FROM connection WHERE sender_id= :id)\n" +
          "union (SELECT sender_id FROM connection WHERE receiver_id= :id)) and u.email LIKE %:keyword%",
          nativeQuery =
                  true)
  Iterable<Users> findUserWithoutConnectionRequest(@Param("id") Long id, @Param("keyword") String keyword);

  @Query(value = "SELECT * FROM users WHERE id in (SELECT sender_id FROM users u inner join " +
          "connection c on c.receiver_id=u.id WHERE receiver_id= :id AND status='INACTIVE')",
          nativeQuery =
                  true)
  List<Users> findConnectionRequest(@Param("id") Long id);

  @Query(value = "SELECT * FROM users WHERE id in (\n" +
          "SELECT sender_id\n" +
          "FROM users u\n" +
          "    inner join connection c\n" +
          "        on c.receiver_id=u.id WHERE  receiver_id= :id\n" +
          "                                AND status='ACTIVE')\n" +
          "union\n" +
          "SELECT * FROM users WHERE id in (\n" +
          "SELECT receiver_id FROM\n" +
          "                       users u\n" +
          "                           inner join connection c\n" +
          "                               on c.receiver_id=u.id WHERE sender_id= :id AND " +
          "status='ACTIVE');",
          nativeQuery = true)
  List<Users> findUserConnections(@Param("id") Long id);

  @Query(value = "select * from users\n" +
          "where id in (\n" +
          "    select u.id\n" +
          "    from users u\n" +
          "             inner join user_role ur on u.id = ur.user_id\n" +
          "             inner join role r on ur.role_id = r.role_id\n" +
          "    where r.name='ROLE_ADMIN'\n" +
          ")", nativeQuery =
          true)
  Users findUserAdmin();

  Page<Users> findByEmailContaining(String keyword, Pageable pageable);
}
