package com.mjtech.mybuddy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class ContactTest {

    private Contact contact1;

    private Contact contact2;


    @BeforeEach
    private void setUp() {
        contact1 = new Contact(1,
                "laplume",
                "laplume",
                "laplume@yahoo.fr",
                "information",
                "demande d'info",
                new Date());

        contact2 = new Contact(1,
                "laplume",
                "laplume",
                "laplume@yahoo.fr",
                "information",
                "demande d'info",
                new Date());
    }

    @Test
    void setId() {
        // GIVEN
        Contact contact = new Contact();
        contact.setId(1);
        contact.setFirstname("eric");
        contact.setLastname("eric");
        contact.setSubject("information");
        contact.setMessage("demande d'info");
        contact.setCreatedAt(new Date());

        // WHEN
        // THEN
        assertThat(1).isEqualTo(contact.getId());
    }

    @Test
    void testEquals() {
        // GIVEN
        // WHEN
        // THEN
        assertThat(contact1.toString().equals(contact2.toString())).isEqualTo(true);
    }

    @Test
    void canEqual() {
        // GIVEN
        // WHEN
        // THEN
        assertThat(contact1.toString()).isEqualTo(contact2.toString());
    }

    @Test
    void testHashCode() {
        // GIVEN
        // WHEN
        // THEN
        assertThat(contact1.toString().hashCode()).isEqualTo(contact2.toString().hashCode());
    }
}