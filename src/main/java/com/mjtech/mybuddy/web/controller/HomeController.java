package com.mjtech.mybuddy.web.controller;

import com.mjtech.mybuddy.model.Contact;
import com.mjtech.mybuddy.web.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Date;


/**
 * HomeController. class that manage
 * request/response logic of user.
 */
@Controller
public class HomeController {

  private static final Logger log = LoggerFactory.getLogger(HomeController.class);
  private final ContactService contactService;

  public HomeController(ContactService contactService) {
    this.contactService = contactService;
  }

  /**
   * index. Method that display a home page.
   *
   * @return home view
   */
  @GetMapping("/")
  public String index() {

    log.info("Displaying home page");

    return "home";
  }


  /**
   * contact. Method that display a contact page.
   *
   * @return contact view
   */
  @GetMapping("/contact")
  public String contact(Model model) {
    model.addAttribute("contact", new Contact());

    log.info("Displaying contact page");

    return "contact";
  }

  /**
   * addMessage. Method that handle post
   * request for sending new contact message.
   *
   * @param contact a contact
   * @param result  a BindingResult
   * @return home view
   */
  @PostMapping("/contact/add")
  public String sendMessage(@Valid Contact contact, BindingResult result) {

    if (result.hasErrors()) {
      log.error("{}, fields are blank", result.getErrorCount());
      return "contact";
    }

    contact.setCreatedAt(new Date());

    contactService.saveContact(contact);

    log.info("Message send successfully!");
    return "home";
  }

}
