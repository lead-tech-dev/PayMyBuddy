package com.mjtech.mybuddy.web.service;

import com.mjtech.mybuddy.model.Contact;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * ContactService interface structure the business logic
 * of contact.
 */
public interface ContactService {

  /**
   * getContacts. Method that get contact list from database.
   *
   * @return Iterable contact
   */
  Iterable<Contact> getContacts();

  /**
   * saveContact. Method that save given contact in database.
   *
   * @param contact a contact
   * @return a contact
   */
  Contact saveContact(Contact contact);

  /**
   * findPage. Method that get contact page from database.
   *
   * @param pageNumber a requested page
   * @return a contact page
   */
  Page<Contact> findPage(int pageNumber);

  /**
   * deleteContact. Method that delete given contact in database.
   *
   * @param id a contact id
   */
  void deleteContact(long id);

  /**
   * getOneContact. Method that get one contact from database.
   *
   * @param id a contact id
   * @return optional contact
   */
  Optional<Contact> getOneContact(long id);
}
