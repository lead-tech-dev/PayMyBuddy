package com.mjtech.mybuddy.web.controller;

import com.mjtech.mybuddy.dto.BankDto;
import com.mjtech.mybuddy.dto.PasswordEditDto;
import com.mjtech.mybuddy.dto.SignupDto;
import com.mjtech.mybuddy.enumeration.Status;
import com.mjtech.mybuddy.model.Account;
import com.mjtech.mybuddy.model.Bank;
import com.mjtech.mybuddy.model.Connection;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.model.security.Role;
import com.mjtech.mybuddy.model.security.UserRole;
import com.mjtech.mybuddy.utility.SecurityUtility;
import com.mjtech.mybuddy.web.service.AccountService;
import com.mjtech.mybuddy.web.service.BankService;
import com.mjtech.mybuddy.web.service.ConnectionService;
import com.mjtech.mybuddy.web.service.UserService;
import com.mjtech.mybuddy.web.service.impl.UserSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.mjtech.mybuddy.config.OAuth2Provider.facebook;
import static com.mjtech.mybuddy.config.OAuth2Provider.google;

/**
 * AccountController. class that manage
 * request/response logic of user account.
 */
@Controller
public class AccountController {

  private static final Logger log = LoggerFactory.getLogger(AccountController.class);
  private static final String authorizationRequestBaseUri = "oauth2/authorization";

  @Autowired
  private UserService userService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private UserSecurityService userSecurityService;


  @Autowired
  private BankService bankService;

  @Autowired
  private ConnectionService connectionService;




  /**
   * login. Method that display a login page.
   *
   * @param model a model
   * @return login view
   */

  @GetMapping("/login")
  public String login(Model model) {
    model.addAttribute(facebook.toString(), authorizationRequestBaseUri + "/" + facebook.toString());
    model.addAttribute(google.toString(), authorizationRequestBaseUri + "/" + google.toString());

    log.info("Displaying login page!");
    return "login";
  }

  /**
   * signup. Method that display a signup page.
   *
   * @param model a model
   * @return signup view
   */
  @GetMapping("/signup")
  public String signup(Model model) {
    model.addAttribute("signupDto", new SignupDto());
    model.addAttribute(facebook.toString(), authorizationRequestBaseUri + "/" + facebook.toString());
    model.addAttribute(google.toString(), authorizationRequestBaseUri + "/" + google.toString());

    log.info("Displaying signup page!");
    return "signup";
  }

  /**
   * getOneUserPage. Method that display current myProfile page number.
   *
   * @param model       a model
   * @param currentPage a current page number
   * @param principal   an authenticated user
   * @return myProfile view
   */
  @GetMapping("/myProfile/page/{pageNumber}")
  public String getOneUserPage(Model model,
                               @PathVariable("pageNumber") int currentPage, Principal principal) {

    boolean existBankAccount = false;

    Page<Users> page = userService.findPage(currentPage);

    Users user = userService.getUserInfo(principal);

    Optional<Account> account = accountService.getOneAccountByUser(user);

    Optional<Bank> bank = bankService.getOneBank(user);

    if (bank.isPresent()) {
      existBankAccount = true;
    }

    List<Users> inactiveConnection = userService.getConnectionRequest(user.getId());

    List<Users> activeConnection = userService.getUserConnectionsList(user.getId());

    int totalPages = page.getTotalPages();
    long totalItems = page.getTotalElements();
    List<Users> users = page.getContent();

    model.addAttribute("currentPage", currentPage);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalItems", totalItems);
    model.addAttribute("users", users);
    model.addAttribute("inactiveConnection", inactiveConnection);
    model.addAttribute("activeConnection", activeConnection);

    model.addAttribute("balance", account.get().getBalance());
    model.addAttribute("existBankAccount", existBankAccount);
    model.addAttribute("user", user);

    log.info("Displaying myProfile page!");
    return "myProfile";
  }

  /**
   * myProfile. Method that display first page of myProfile.
   *
   * @param model     a model
   * @param principal an authenticated user
   * @return myProfile view
   */
  @GetMapping("/myProfile")
  public String myProfile(Model model, Principal principal) {
    return getOneUserPage(model, 1, principal);
  }


  /**
   * newUser. Method that handle a post request for
   * creating new user.
   *
   * @param model     a model
   * @param signupDto a signupDto
   * @param result    a BindingResult
   * @return login view
   */
  @PostMapping(value = "/newUser")
  public String newUser(
          @Valid SignupDto signupDto, BindingResult result,
          Model model
  ) throws Exception {
    model.addAttribute("email", signupDto.getEmail());
    model.addAttribute("username", signupDto.getUsername());
    Users admin = userService.findAdminUser();

    if (result.hasErrors()) {
      log.error("{}, fields are blank", result.getErrorCount());
      return "signup";
    }

    if (userService.findByEmail(signupDto.getEmail()) != null) {
      log.error("{}, not exist!", signupDto.getEmail());
      result.rejectValue("email", "error.emailExists", "Email already exists!");
      ;

      return "signup";
    }

    if (!Objects.equals(signupDto.getPassword(), signupDto.getRetypePassword())) {
      log.error("password doesn't match!");
      result.rejectValue("retypePassword", "error.passwordNotMatch", "Passwords doesn t match!");
      return "signup";
    }

    Users user = new Users();
    user.setEmail(signupDto.getEmail());
    user.setUsername(signupDto.getUsername());
    user.setName(signupDto.getUsername());


    String encryptedPassword = SecurityUtility.passwordEncoder().encode(signupDto.getPassword());
    user.setPassword(encryptedPassword);

    Role role = new Role();
    role.setRoleId(1);
    role.setName("ROLE_USER");
    Set<UserRole> userRoles = new HashSet<>();
    userRoles.add(new UserRole(user, role));


    userService.createUser(user, userRoles);

    Account account = new Account();
    account.setCreatedAt(new Date());
    account.setBalance(0);
    account.setUser(user);
    accountService.saveAccount(account);

    Connection connection = new Connection();
    connection.setReceiver(user);
    connection.setCreatedAt(new Date());
    connection.setSender(admin);
    connection.setStatus(Status.ACTIVE);
    connectionService.saveConnection(connection);

    log.error("{}, successful added!", user.getUsername());
    return "login";
  }

  /**
   * loginUser. Method that handle a post request for
   * login new.
   *
   * @param model    a model
   * @param email    a user email
   * @param password a user password
   * @return myProfile view
   */
  @PostMapping(value = "/loginUser")
  public String loginUser(@ModelAttribute("email") String email, @ModelAttribute("password") String password,
                          Model model
  ) throws Exception {

    UserDetails userDetails = userSecurityService.loadUserByUsername(email);

    if (userDetails == null) {
      log.error("error credentials!");
      model.addAttribute("errorCredentials", true);
      return "login";
    }

    if (!SecurityUtility.passwordEncoder().matches(password, userDetails.getPassword())) {
      log.error("error credentials!");
      model.addAttribute("errorCredentials", true);
      return "login";
    }


    Authentication
            authentication = new UsernamePasswordAuthenticationToken(userDetails,
            userDetails.getPassword(),
            userDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authentication);

    log.info("{}, successful logged", email);
    return "redirect:/myProfile";
  }

  /**
   * editPassword. Method that display an
   * edit password page.
   *
   * @param model a model
   * @return edit-password view
   */
  @GetMapping("/myProfile/password")
  public String editPassword(Model model) {
    model.addAttribute("passwordEditDto", new PasswordEditDto());
    log.info("Displaying edit password page!");
    return "user/edit-password";
  }

  /**
   * editPassword. Method that handle a post request for
   * password edit.
   *
   * @param passwordEditDto a passwordEditDto
   * @param result          a BindingResult
   * @param principal       an authenticated user
   * @return login view
   */

  @PostMapping("/myProfile/password/edit")
  public String editPassword(@Valid PasswordEditDto passwordEditDto, BindingResult result,
                             Principal principal) {

    if (result.hasErrors()) {
      log.error("{}, fields are blank", result.getErrorCount());
      return "user/edit-password";
    }

    Users user = userService.getUserInfo(principal);


    if (!SecurityUtility.passwordEncoder().matches(passwordEditDto.getOldPassword(),
            user.getPassword())) {
      log.error("Incorrect old password");
      result.rejectValue("oldPassword", "error.incorrectOldPassword", "Incorrect old password!");
      return "user/edit-password";
    }


    if (!Objects.equals(passwordEditDto.getRetypedNewPassword(), passwordEditDto.getNewPassword())) {
      log.error("Password doesn't match!");
      result.rejectValue("retypedNewPassword", "error.passwordNotMatch", "Password doesn t match!");
      return "user/edit-password";
    }

    String encryptedPassword = SecurityUtility.passwordEncoder().encode(passwordEditDto.getNewPassword());
    user.setPassword(encryptedPassword);

    userService.updateUser(user);

    log.info("Password successfully edited!");

    return "redirect:/logout";
  }


  /**
   * addBankAccount. Method that display an
   * add bank page.
   *
   * @param model a model
   * @return add-bank view
   */
  @GetMapping("/myProfile/user/bankAccount")
  public String addBankAccount(Model model) {
    model.addAttribute("bankDto", new BankDto());

    log.info("Displaying add bank account page");

    return "user/add-bank";
  }

  /**
   * addBankAccount. Method that handle a post request for
   * add bank.
   *
   * @param bankDto   a bankDto
   * @param result    a BindingResult
   * @param principal an authenticated user
   * @return myProfile view
   */
  @PostMapping("/myProfile/user/bankAccount/add")
  public String addBankAccount(@Valid BankDto bankDto, BindingResult result, Model model,
                               Principal principal) {

    if (result.hasErrors()) {
      log.error("{}, fields are blank", result.getErrorCount());
      return "user/add-bank";
    }

    Users user = userService.getUserInfo(principal);

    Bank bank = new Bank();
    bank.setBankAccountName(bankDto.getBankAccountName());
    bank.setIban(bankDto.getIban());
    bank.setDescription(bankDto.getDescription());
    bank.setBalance(0);
    bank.setUser(user);

    Connection connection = new Connection(user, user);
    connection.setStatus(Status.ACTIVE);
    connectionService.saveConnection(connection);

    bankService.saveBank(bank);

    log.info("bank account successfully saved!");

    return "redirect:/myProfile";
  }

/*
  private User getOAuth2LoginInfo(Principal user) {
    StringBuffer protectedInfo = new StringBuffer();
    OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
    OAuth2User principal = ((OAuth2AuthenticationToken) user).getPrincipal();

    //OidcIdToken idToken = getIdToken(principal);

   *//* if(idToken != null) {

      protectedInfo.append("idToken value: " + idToken.getTokenValue()+"<br><br>");
      protectedInfo.append("Token mapped values <br><br>");

      Map<String, Object> claims = idToken.getClaims();

      for (String key : claims.keySet()) {
        protectedInfo.append("  " + key + ": " + claims.get(key)+"<br>");
      }
    }*//*

    OAuth2AuthorizedClient authClient =
        this.auth2AuthorizedClientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(),
        authToken.getName());

    Map<String, Object> userDetails =
        ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

    String userToken = authClient.getAccessToken().getTokenValue();
    protectedInfo.append("Welcom, " + userDetails.get("name"));
    protectedInfo.append("email, " + userDetails.get("email"));
    protectedInfo.append("Access token, " + userToken + "<br><br>");
    return new User(userDetails.get("name").toString(), userDetails.get("email").toString());
  }

  private User getUsernamePasswordLoginInfo(Principal principal) {
    StringBuffer usernameInfo = new StringBuffer();
    UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) principal);
    User user = null;
    if (token.isAuthenticated()) {
      user  = (User) token.getPrincipal();
    }else {
      usernameInfo.append("NA");
    }
    return user;
  }

  private OidcIdToken getIdToken(OAuth2User principal){
    if(principal instanceof DefaultOidcUser) {
      DefaultOidcUser oidcUser = (DefaultOidcUser)principal;
      return oidcUser.getIdToken();
    }
    return null;
  }*/
}
