package com.mjtech.mybuddy.repository;

import com.mjtech.mybuddy.model.Contact;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * ContactRepository. Interface that allows to access and
 * persist data between Contact object and buddy database
 */
@Repository
public interface ContactRepository extends PagingAndSortingRepository<Contact, Long> {
}
