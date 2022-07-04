package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.model.Contact;
import com.mjtech.mybuddy.repository.ContactRepository;
import com.mjtech.mybuddy.web.service.ContactService;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Tag("ContactServiceImplTest")
@DisplayName("contact service implementation class test")
class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;

    private ContactService contactService;

    private Contact contact;

    @BeforeEach
    private void setUp() {
        contactService = new ContactServiceImpl(contactRepository);

        contact = new Contact(
                1,
                "maximan",
                "eric",
                "eric@yahoo.fr",
                "demande de remboursement",
                "j'aimerai me faire rembourser svp",
                new Date()
        );

    }

    @Test
    @DisplayName("getContacts should return contact list")
    void getContacts_ShouldReturnContactList() {
        // GIVEN
        when(contactRepository.findAll()).thenReturn(List.of(contact));

        // WHEN
        Iterable<Contact> results = contactService.getContacts();

        // THEN
        verify(contactRepository, times(1)).findAll();
        assertThat(results).contains(contact);
    }

    @Test
    @DisplayName("saveContact should return saved contact for given contact")
    void saveContact_ShouldReturnSavedContact_ForGivenContact() {
        // GIVEN
        when(contactRepository.save(contact)).thenReturn(contact);

        // WHEN
        Contact result = contactService.saveContact(contact);

        // THEN
        verify(contactRepository, times(1)).save(contact);
        assertThat(result).isEqualTo(contact);
    }

    @Test
    @DisplayName("findPage, should return contact page list for given page number")
    void findPage_ShouldReturnContactPageList_ForGivenPageNumber() {
        // GIVEN
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        Page<Contact> contacts = new PageImpl(List.of(contact));
        when(contactRepository.findAll(pageable)).thenReturn(contacts);

        // WHEN
        Page<Contact> result = contactService.findPage(pageNumber);

        // THEN
        verify(contactRepository, times(1)).findAll(pageable);
        assertThat(result).contains(contact);
    }

    @Test
    @DisplayName("deleteContact, should delete contact for given contact id")
    void deleteContact_ShouldDeleteContact_ForGivenContactId() {
        // GIVEN
        doNothing().when(contactRepository).deleteById(contact.getId());

        // WHEN
        contactService.deleteContact(contact.getId());

        // THEN
        verify(contactRepository, times(1)).deleteById(contact.getId());
        assertTrue(contactService.getOneContact(contact.getId()).isEmpty());
    }

    @Test
    @DisplayName("getOneContact, should return contact for given contact id")
    void getOneContact_ShouldReturnContact_ForGivenContactId() {
        // GIVEN
        when(contactRepository.findById(contact.getId())).thenReturn(Optional.ofNullable(contact));

        // WHEN
        Optional<Contact> result = contactService.getOneContact(contact.getId());

        // THEN
        verify(contactRepository, times(1)).findById(contact.getId());
        assertThat(result.get()).isEqualTo(contact);
    }
}