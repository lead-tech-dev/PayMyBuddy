package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.model.Contact;
import com.mjtech.mybuddy.repository.ContactRepository;
import com.mjtech.mybuddy.web.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * ContactServiceImpl. class that implement
 * contact business logic
 */
@Service
public class ContactServiceImpl implements ContactService {

  private final ContactRepository contactRepository;

  public ContactServiceImpl(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Contact> getContacts() {
    return contactRepository.findAll();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Contact saveContact(Contact contact) {
    return contactRepository.save(contact);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Page<Contact> findPage(int pageNumber) {
    Pageable pageable = PageRequest.of(pageNumber - 1, 5);
    return contactRepository.findAll(pageable);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteContact(long id) {
    contactRepository.deleteById(id);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Contact> getOneContact(long id) {
    return contactRepository.findById(id);
  }


}
