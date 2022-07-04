package com.mjtech.mybuddy.web.controller;

import com.mjtech.mybuddy.dto.CouponRechargeDto;
import com.mjtech.mybuddy.dto.TransferDto;
import com.mjtech.mybuddy.enumeration.Status;
import com.mjtech.mybuddy.enumeration.TransferType;
import com.mjtech.mybuddy.model.Account;
import com.mjtech.mybuddy.model.Bank;
import com.mjtech.mybuddy.model.Connection;
import com.mjtech.mybuddy.model.Coupon;
import com.mjtech.mybuddy.model.Transfer;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.web.service.AccountService;
import com.mjtech.mybuddy.web.service.BankService;
import com.mjtech.mybuddy.web.service.ConnectionService;
import com.mjtech.mybuddy.web.service.CouponService;
import com.mjtech.mybuddy.web.service.TransferService;
import com.mjtech.mybuddy.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * UserController. class that manage
 * request/response logic of user.
 */
@Controller
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  private final CouponService couponService;

  private final UserService userService;

  private final AccountService accountService;

  private final ConnectionService connectionService;

  private final TransferService transferService;

  private final BankService bankService;

  public UserController(CouponService couponService, UserService userService,
                        AccountService accountService, ConnectionService connectionService,
                        TransferService transferService, BankService bankService) {
    this.couponService = couponService;
    this.userService = userService;
    this.accountService = accountService;
    this.connectionService = connectionService;
    this.transferService = transferService;
    this.bankService = bankService;
  }

  /**
   * recharge. Method that display a recharge page.
   *
   * @param model a model
   * @return recharge view
   */
  @GetMapping("/myProfile/user/recharge")
  public String recharge(Model model) {
    log.info("displaying recharge page ...");
    model.addAttribute("couponRechargeDto", new CouponRechargeDto());
    return "user/recharge";
  }

  /**
   * recharge. Method that handle a post request for
   * recharging user account.
   *
   * @param model             a model
   * @param couponRechargeDto a couponRechargeDto
   * @param result            a BindingResult
   * @param principal         an authenticated user
   * @return myProfile view
   */
  @PostMapping("/myProfile/user/recharge/add")
  public String recharge(@Valid CouponRechargeDto couponRechargeDto, BindingResult result,
                         Model model, Principal principal) {


    if (result.hasErrors()) {
      log.error("{} fields are blank", result.getErrorCount());
      return "user/recharge";
    }

    if (!couponRechargeDto.getCouponId().matches("^[0-9]*$") && couponRechargeDto.getCouponId().length() > 2) {
      log.error("incorrect coupon code!");
      result.rejectValue("couponId", "error.incorrectCouponIdType", "Please enter a correct coupon code!");
      return "user/recharge";
    }

    Users user = userService.getUserInfo(principal);
    Users admin = userService.findAdminUser();
    Optional<Account> account = accountService.getOneAccountByUser(user);
    Optional<Coupon> coupon = couponService.getOneCoupon(couponRechargeDto.getCouponId());

    if (coupon.isEmpty()) {
      log.error("Not exists coupon code!");
      result.rejectValue("couponId", "error.couponNotExist", "Coupon code not exists!");
      return "user/recharge";
    }

    if (coupon.get().getStatus() == Status.INACTIVE) {
      log.error("inactive coupon code!");
      result.rejectValue("couponId", "error.couponInactive", "Inactive coupon code!");
      return "user/recharge";
    }

    if (coupon.get().getExpiredAt().getTime() < new Date().getTime()) {
      log.error("expired coupon code!");
      result.rejectValue("couponId", "error.couponExpired", "Expired coupon code!");
      return "user/recharge";
    }

    account.get().setBalance(account.get().getBalance() + coupon.get().getPrice());
    accountService.saveAccount(account.get());

    coupon.get().setStatus(Status.INACTIVE);

    couponService.saveCoupon(coupon.get());
    Optional<Connection> connection = connectionService.findOneConnection(user, admin);

    Transfer transfer = new Transfer();
    transfer.setDescription("Coupon recharge");
    transfer.setAmount(coupon.get().getPrice());
    transfer.setTransferDate(new Date());
    transfer.setCommission(0);
    transfer.setCredit(admin);
    transfer.setDebtor(user);
    transfer.setConnection(connection.get());
    transfer.setType(TransferType.RECHARGE);

    transferService.saveTransfer(transfer);

    log.info("success recharging account!");

    return "redirect:/myProfile";
  }

  /**
   * getOneUserPage. Method that display add-connection
   * page given current page number.
   *
   * @param model     a model
   * @param keyword   a search keyword
   * @param principal an authenticated user
   * @return add-connection view
   */
  @GetMapping("/myProfile/user/add-connection")
  public String getOneUserPage(Model model, @RequestParam(value = "keyword", defaultValue = "") String keyword,
                               Principal principal) {
    Users user = userService.getUserInfo(principal);
    Iterable<Users> users = userService.findUserWithoutConnectionRequest(user.getId(), keyword);

    model.addAttribute("users", users);
    model.addAttribute("user", user);
    log.info("displaying add-connection page...");
    return "add-connection";
  }


  /**
   * sendConnection. Method that handle get
   * request for sending connection request.
   *
   * @param model     a model
   * @param userId    a user to add connection
   * @param principal an authenticated user
   * @return add-connection view
   */
  @GetMapping("/myProfile/user/add-connection/add/{userId}")
  public String sendConnection(Model model, @PathVariable String userId, Principal principal) {

    Optional<Users> receiver = userService.findById(Long.parseLong(userId));

    if (receiver.isEmpty()) {
      log.info("Receiver user not found with id {}", userId);
      return "redirect:/myProfile/user/add-connection";
    }

    Users sender = userService.getUserInfo(principal);

    Optional<Connection> existConnection = connectionService.findOneConnection(sender,
            receiver.get());

    if (existConnection.isPresent()) {
      log.info("Connection {} already exist ", existConnection.get());
      return "redirect:/myProfile/user/add-connection";
    }

    Connection connection = new Connection(sender, receiver.get());

    connectionService.saveConnection(connection);

    log.info("Connection successful added!");
    return "redirect:/myProfile/user/add-connection";
  }

  /**
   * acceptConnection. Method that handle get
   * request for accepting connection request.
   *
   * @param model     a model
   * @param userId    a user to accept connection
   * @param principal an authenticated user
   * @return add-connection view
   */
  @GetMapping("/myProfile/user/add-connection/accept/{userId}")
  public String acceptConnection(Model model, @PathVariable String userId, Principal principal) {
    Users currentUser = userService.getUserInfo(principal);
    Optional<Users> acceptedConnectionUser = userService.findById(Long.parseLong(userId));

    if (acceptedConnectionUser.isEmpty()) {
      log.info("user with id {} not found ", userId);
      return "redirect:/myProfile";
    }

    Optional<Connection> connection = connectionService.findOneConnection(currentUser,
            acceptedConnectionUser.get());


    if (connection.isEmpty()) {
      log.info("Connection not found ");
      return "redirect:/myProfile";
    }

    connection.get().setStatus(Status.ACTIVE);

    connectionService.saveConnection(connection.get());

    log.info("{} and {} are now connected!", currentUser.getUsername(), acceptedConnectionUser.get().getUsername());
    return "redirect:/myProfile";
  }

  /**
   * deleteConnection. Method that handle delete
   * request for deleting connection request.
   *
   * @param model     a model
   * @param userId    a user to delete connection
   * @param principal an authenticated user
   * @return add-connection view
   */
  @GetMapping("/myProfile/user/add-connection/remove/{userId}")
  public String deleteConnection(Model model, @PathVariable String userId, Principal principal) {
    Users currentUser = userService.getUserInfo(principal);
    Optional<Users> removedConnectionUser = userService.findById(Long.parseLong(userId));

    if (removedConnectionUser.isEmpty()) {
      log.info("user with id {} not found", userId);
      return "redirect:/myProfile";
    }

    Optional<Connection> connection = connectionService.findOneConnection(currentUser,
            removedConnectionUser.get());

    if (connection.isEmpty()) {
      log.info("Connection not found");
      return "redirect:/myProfile";
    }

    connectionService.deleteConnection(connection.get().getId());

    log.info("Connection {} is removed ", connection.get());
    return "redirect:/myProfile";
  }

  /**
   * getOneTransferPage. Method that display transfer page
   * given current page number.
   *
   * @param model       a model
   * @param currentPage a current page number
   * @param principal   an authenticated user
   * @return transfer view
   */
  @GetMapping("/myProfile/user/transfer/page/{pageNumber}")
  public String getOneTransferPage(Model model, @PathVariable("pageNumber") int currentPage, Principal principal) {

    boolean existBankAccount = false;

    Users user = userService.getUserInfo(principal);

    Page<Transfer> page = transferService.getTransaction(user.getId(), currentPage);

    List<Users> connections = userService.getUserConnectionsList(user.getId());

    Optional<Bank> bank = bankService.getOneBank(user);

    if (bank.isPresent()) {
      existBankAccount = true;
    }

    int totalPages = page.getTotalPages();
    long totalItems = page.getTotalElements();
    List<Transfer> transactions = page.getContent();

    System.out.println(transactions);

    model.addAttribute("transferDto", new TransferDto());
    model.addAttribute("connections", connections);
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalItems", totalItems);
    model.addAttribute("transactions", transactions);
    model.addAttribute("user", user);
    model.addAttribute("friendTransfer", true);
    model.addAttribute("existBankAccount", existBankAccount);
    model.addAttribute("bank", new Bank());

    log.info("Displaying transfer page");
    return "transfer";
  }

  /**
   * transfer. Method that display first page of transfer.
   *
   * @param model     a model
   * @param principal an authenticated user
   * @return transfer view
   */
  @GetMapping("/myProfile/user/transfer")
  public String transfer(Model model, Principal principal) {
    return getOneTransferPage(model, 1, principal);
  }

  /**
   * addTransfer. Method that handle post
   * request for adding new transfer.
   *
   * @param model       a model
   * @param transferDto a transferDto
   * @param principal   an authenticated user
   * @return transfer view
   */
  @PostMapping("/myProfile/user/transfer/add")
  public String addTransfer(@Valid TransferDto transferDto, Model model,
                            Principal principal) {

    Users admin = userService.findAdminUser();
    Optional<Account> adminAccount = accountService.getOneAccountByUser(admin);

    Users currentUser = userService.getUserInfo(principal);
    Optional<Account> currentUserAccount = accountService.getOneAccountByUser(currentUser);

    Optional<Users> sendToUser = userService.findById(Long.parseLong(transferDto.getTransfer()));

    if (sendToUser.isEmpty()) {
      log.info("user for id {} not found ", transferDto.getTransfer());
      return "redirect:/myProfile/user/transfer";
    }

    Optional<Account> sendToUserAccount = accountService.getOneAccountByUser(sendToUser.get());

    Optional<Connection> connection = connectionService.findOneConnection(currentUser,
            sendToUser.get());

    if (connection.isEmpty()) {
      log.info("Connection  not found ");
      return "redirect:/myProfile/user/transfer";
    }


    if (currentUserAccount.get().getBalance() <= 0 || currentUserAccount.get().getBalance() < transferDto.getAmount()) {
      model.addAttribute("balanceLimit", true);
      log.info("{}, insufficient balance", currentUser.getUsername());
      return "redirect:/myProfile/user/transfer";
    }

    double commission = (double) transferDto.getAmount() * 5 / 100;

    currentUserAccount.get().setBalance(currentUserAccount.get().getBalance() - transferDto.getAmount() - commission);

    sendToUserAccount.get().setBalance(sendToUserAccount.get().getBalance() + transferDto.getAmount());

    adminAccount.get().setBalance(adminAccount.get().getBalance() + commission);

    accountService.saveAccount(currentUserAccount.get());
    accountService.saveAccount(sendToUserAccount.get());
    accountService.saveAccount(adminAccount.get());

    Transfer transfer = new Transfer();

    transfer.setDescription(transferDto.getDescription());
    transfer.setAmount(transferDto.getAmount());
    transfer.setTransferDate(new Date());
    transfer.setCommission(commission);
    transfer.setCredit(currentUser);
    transfer.setDebtor(sendToUser.get());
    transfer.setConnection(connection.get());
    transfer.setType(TransferType.OPERATION);

    transferService.saveTransfer(transfer);

    log.info("Transfer successful!");

    return "redirect:/myProfile/user/transfer";
  }


  /**
   * bankTransfer. Method that handle post
   * request for adding new bank transfer.
   *
   * @param model     a model
   * @param amount    a transfer amount
   * @param principal an authenticated user
   * @return transfer view
   */
  @PostMapping("/myProfile/user/transfer/bank")
  public String bankTransfer(@ModelAttribute("amount") int amount, Model model, Principal principal) {

    Users user = userService.getUserInfo(principal);
    Users admin = userService.findAdminUser();
    Optional<Account> account = accountService.getOneAccountByUser(user);
    Optional<Account> adminAccount = accountService.getOneAccountByUser(admin);
    Optional<Bank> bank = bankService.getOneBank(user);
    Optional<Connection> connection = connectionService.findOneConnection(user,
            user);

    if (account.get().getBalance() <= 0 || account.get().getBalance() < amount) {
      model.addAttribute("balanceLimit", true);
      log.info("{}, insufficient balance", user.getUsername());
      return "redirect:/myProfile/user/transfer";
    }

    double commission = (double) amount * 5 / 100;

    bank.get().setBalance(bank.get().getBalance() + amount);

    account.get().setBalance(account.get().getBalance() - amount - commission);

    adminAccount.get().setBalance(adminAccount.get().getBalance() + commission);

    accountService.saveAccount(account.get());
    bankService.saveBank(bank.get());
    accountService.saveAccount(adminAccount.get());

    Transfer transfer = new Transfer();
    transfer.setDescription("Tranfert vers mon compte bancaire");
    transfer.setAmount(amount);
    transfer.setTransferDate(new Date());
    transfer.setCommission(commission);
    transfer.setCredit(user);
    transfer.setDebtor(user);
    transfer.setType(TransferType.BANK);
    transfer.setConnection(connection.get());

    transferService.saveTransfer(transfer);
    log.info("Bank transfer successful!");
    return "redirect:/myProfile/user/transfer";
  }
}
