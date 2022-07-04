package com.mjtech.mybuddy.repository;

import com.mjtech.mybuddy.model.security.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * RoleRepository. Interface that allows to access and
 * persist data between Role object and buddy database
 */
public interface RoleRepository extends CrudRepository<Role, Long> {
  Role findByName(String name);
}
