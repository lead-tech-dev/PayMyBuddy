package com.mjtech.mybuddy.web.controller;

import com.mjtech.mybuddy.model.Account;
import com.mjtech.mybuddy.model.Contact;
import com.mjtech.mybuddy.model.Coupon;
import com.mjtech.mybuddy.model.Transfer;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.web.service.AccountService;
import com.mjtech.mybuddy.web.service.ContactService;
import com.mjtech.mybuddy.web.service.CouponService;
import com.mjtech.mybuddy.web.service.TransferService;
import com.mjtech.mybuddy.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


/**
 * AdminController. class that manage
 * request/response logic of  admin.
 */

@Controller
public class AdminController {

  private static final Logger log = LoggerFactory.getLogger(AdminController.class);

  private final CouponService couponService;

  private final ContactService contactService;

  private final TransferService transferService;

  private final UserService userService;

  private final AccountService accountService;

  public AdminController(CouponService couponService, ContactService contactService,
                         TransferService transferService, UserService userService, AccountService accountService) {
    this.couponService = couponService;
    this.contactService = contactService;
    this.transferService = transferService;
    this.userService = userService;
    this.accountService = accountService;
  }

  /**
   * getOneUserPage. Method that display user operation
   * given page number.
   *
   * @param model       a model
   * @param currentPage a current page number
   * @param principal   an authenticated user
   * @return user-details-operation view
   */
  @GetMapping("/myProfile/admin/user/{userID}/page/{currentPage}")
  public String getOneUserPage(Model model, Principal principal,
                               @PathVariable String userID, @PathVariable int currentPage) {

    Optional<Users> user = userService.findById(Long.parseLong(userID));
    Page<Transfer> page = transferService.getTransaction(Long.parseLong(userID), currentPage);
    Optional<Account> account = accountService.getOneAccountByUser(user.get());

    int totalPages = page.getTotalPages();
    long totalItems = page.getTotalElements();
    List<Transfer> transactions = page.getContent();

    model.addAttribute("currentPage", currentPage);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalItems", totalItems);
    model.addAttribute("transactions", transactions);
    model.addAttribute("user", user.get());
    model.addAttribute("balance", account.get().getBalance());

    log.info("Displaying user details operation page!");
    return "user-details-operation";
  }

  /**
   * userOperation. Method that display  user operation first page.
   *
   * @param model     a model
   * @param principal an authenticated admin
   * @return user-details-operation view
   */
  @GetMapping("/myProfile/admin/user/{userID}")
  public String userOperation(Model model, Principal principal, @PathVariable String userID) {
    return getOneUserPage(model, principal, userID, 1);
  }

  /**
   * getOneCouponPage. Method that display coupon page
   * given page number.
   *
   * @param model       a model
   * @param currentPage a current page number
   * @return coupon view
   */
  @GetMapping("/myProfile/admin/coupon/page/{pageNumber}")
  public String getOneCouponPage(Model model, @PathVariable("pageNumber") int currentPage) {
    Page<Coupon> page = couponService.findPage(currentPage);

    int totalPages = page.getTotalPages();
    long totalItems = page.getTotalElements();
    List<Coupon> coupons = page.getContent();

    model.addAttribute("currentPage", currentPage);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalItems", totalItems);
    model.addAttribute("coupons", coupons);

    log.info("Displaying coupon manage page");
    return "coupon";
  }

  /**
   * getAllCouponPages. Method that display
   * coupon page with list of coupon.
   *
   * @param model a model
   * @return coupon view
   */
  @GetMapping("/myProfile/admin/coupon")
  public String getAllCouponPages(Model model) {
    return getOneCouponPage(model, 1);
  }


  /**
   * generate. Method that handle a post request for
   * generate coupon list.
   *
   * @param model        a model
   * @param couponNumber a number of coupon to generate
   * @param price        a price of coupon
   * @return coupon view
   */
  @PostMapping("/myProfile/admin/coupon/generate")
  public String generate(Model model, @ModelAttribute("nbrCoupon") String couponNumber,
                         @ModelAttribute("price") int price) {
    int expiration = 60 * 24;

    couponService.generateCoupon(couponNumber, price, couponService.calculateExpiryDate(expiration));

    log.info("{}, Coupons generated successfully!", couponNumber);

    return "redirect:/myProfile/admin/coupon";
  }

  /**
   * deleteCoupon. Method that handle a delete request for
   * delete coupon.
   *
   * @param model       a model
   * @param id          a coupon id
   * @param currentPage a current page
   * @return coupon view
   */
  @GetMapping("/myProfile/admin/coupon/{id}/{currentPage}")
  public String deleteCoupon(Model model, @PathVariable String id,
                             @PathVariable String currentPage) {

    couponService.deleteCoupon(Integer.parseInt(id));

    log.info("coupon code {}, deleted successfully!", id);

    return "redirect:/myProfile/admin/coupon/page/" + Integer.parseInt(currentPage);
  }


  /**
   * getOneContactPage. Method that display contact page
   * given page number.
   *
   * @param model       a model
   * @param currentPage a current page number
   * @return messages view
   */
  @GetMapping("/myProfile/admin/messages/page/{pageNumber}")
  public String getOneContactPage(Model model, @PathVariable("pageNumber") int currentPage) {
    Page<Contact> page = contactService.findPage(currentPage);

    int totalPages = page.getTotalPages();
    long totalItems = page.getTotalElements();
    List<Contact> messages = page.getContent();

    model.addAttribute("currentPage", currentPage);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalItems", totalItems);
    model.addAttribute("messages", messages);

    log.info("Displaying messages page!");

    return "messages";
  }

  /**
   * getOneContactPage. Method that display first page of messages.
   *
   * @param model a model
   * @return messages view
   */
  @GetMapping("/myProfile/admin/messages")
  public String getAllContactPages(Model model) {
    return getOneContactPage(model, 1);
  }

  /**
   * deleteContact. Method that handle a delete request for
   * delete contact message.
   *
   * @param model       a model
   * @param id          a contact id
   * @param currentPage a current page
   * @return messages view
   */
  @GetMapping("/myProfile/admin/messages/remove/{id}/{currentPage}")
  public String deleteContact(Model model, @PathVariable String id,
                              @PathVariable String currentPage) {

    contactService.deleteContact(Integer.parseInt(id));

    log.info("Message {}, deleted successfully!", id);

    return "redirect:/myProfile/admin/messages/page/" + Integer.parseInt(currentPage);
  }

  /**
   * messageDetails. Method that handle a get request for
   * given contact message id.
   *
   * @param model a model
   * @param id    a contact message id
   * @return message-details view
   */
  @GetMapping("/myProfile/admin/messages/{id}")
  public String messageDetails(Model model, @PathVariable String id) {
    Optional<Contact> contact = contactService.getOneContact(Integer.parseInt(id));

    if (contact.isEmpty()) {
      log.info("Message {}, not found!", id);
      return "redirect:/myProfile/admin/messages";
    }

    model.addAttribute("contact", contact.get());
    log.info("Displaying message: {}!", contact.get());
    return "message-details";
  }

  /**
   * deleteUser. Method that handle a delete request for
   * delete user.
   *
   * @param model       a model
   * @param id          a user id
   * @param currentPage a current page
   * @return myProfile view
   */
  @GetMapping("/myProfile/admin/user/remove/{id}/{currentPage}")
  public String deleteUser(Model model, @PathVariable String id,
                           @PathVariable String currentPage) {

    userService.deleteUser(Integer.parseInt(id));
    log.info("Message {}, deleting successfully!", id);
    return "redirect:/myProfile/page/" + Integer.parseInt(currentPage);
  }

}
