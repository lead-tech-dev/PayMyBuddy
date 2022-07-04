package com.mjtech.mybuddy.repository;

import com.mjtech.mybuddy.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * ConnectionRepository. Interface that allows to access and
 * persist data between Connection object and buddy database
 */
@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

  @Query(value = "SELECT * FROM connection  WHERE receiver_id= :id OR sender_id= :id AND " +
          "status='ACTIVE'",
          nativeQuery =
                  true)
  List<Connection> findActiveConnection(@Param("id") Long id);

  @Query(value = "SELECT * FROM connection  WHERE sender_id= :sender_id AND receiver_id= " +
          ":receiver_id OR sender_id= :receiver_id AND receiver_id= :sender_id ",
          nativeQuery =
                  true)
  Optional<Connection> findOneConnection(@Param("sender_id") Long sender_id,
                                         @Param("receiver_id") Long receiver_id);

}
