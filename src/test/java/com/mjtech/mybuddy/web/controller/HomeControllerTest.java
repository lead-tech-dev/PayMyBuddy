package com.mjtech.mybuddy.web.controller;

import com.mjtech.mybuddy.web.AbstractTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HomeControllerTest extends AbstractTest {

    @Test
    @DisplayName("index, should display home page")
    void index_ShouldDisplayHomePage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(view().name("home"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("contact should display contact page")
    void contact_ShouldDisplayContactPage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/contact"))
                .andExpect(view().name("contact"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("addMessageBlankFieldError, should display error message for given blank field")
    void addMessageBlankFieldError_ShouldDisplayErrorMessage_ForGivenBlankField() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/contact/add")
                        .param("firstname", "")
                        .param("lastname", "")
                        .param("email", "")
                        .param("subject", "")
                        .param("message", ""))
                .andExpect(model().attributeHasFieldErrors(
                        "contact", "firstname", "lastname", "email", "subject", "message"))
                .andExpect(content().string(containsString("Firstname field can not be blank")))
                .andExpect(content().string(containsString("Lastname field can not be blank")))
                .andExpect(content().string(containsString("Email field can not be blank")))
                .andExpect(content().string(containsString("Subject field can not be blank")))
                .andExpect(content().string(containsString("Message field can not be blank")))
                .andExpect(view().name("contact"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("addMessageSuccess, should redirect to home page")
    void addMessageSuccess_ShouldRedirectToHomePage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/contact/add")
                        .param("firstname", "maximus")
                        .param("lastname", "maximus")
                        .param("email", "maximus@yahoo.fr")
                        .param("subject", "demande d'info")
                        .param("message", "serait il possible de supprimer son compte ?"))
                .andExpect(view().name("home"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }
}