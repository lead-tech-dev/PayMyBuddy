package com.mjtech.mybuddy.web.controller;


import com.mjtech.mybuddy.web.AbstractTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("AccountControllerTest")
@DisplayName("Account controller test logic")
class AccountControllerTest extends AbstractTest {

    @Test
    @DisplayName("login should display login page")
    void login_ShouldDisplayLoginPage() throws Exception {
        // GIVEN

        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(view().name("login"))
                .andDo(print())
                .andExpect(status().isOk());

        // THEN
    }

    @Test
    @DisplayName("login should display signup page")
    void signup_ShouldDisplaySignupPage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/signup"))
                .andExpect(view().name("signup"))
                .andDo(print())
                .andExpect(status().isOk());
        // THEN
    }

    @Test
    @DisplayName("login should display user myProfile page")
    @WithUserDetails("jean@yahoo.fr")
    void getOneUserPage_ShouldDisplayUserMyProfilePage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile"))
                .andExpect(view().name("myProfile"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("newUserBlankInputFieldError, should display error message for given blank input field")
    void newUserBlankInputFieldError_ShouldReturnValidationErrors_ForGivenBlankInputField() throws Exception {
        // GIVEN
        // WHEN

        this.mvc.perform(MockMvcRequestBuilders.post("/newUser")
                        .param("username", "")
                        .param("email", "")
                        .param("password", "")
                        .param("retypedPassword", ""))
                .andExpect(model().attributeHasFieldErrors(
                        "signupDto", "username", "email", "password", "retypePassword"))
                .andExpect(view().name("signup"))
                .andExpect(status().isOk())
                .andDo(print());

        // THEN
    }

    @Test
    @DisplayName("newUserEmailAlreadyExistError, should display error message for given exist email")
    void newUserEmailAlreadyExistError_ShouldReturnValidationEmailAlreadyExistError_ForGivenExistEmail() throws Exception {
        // GIVEN

        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/newUser")
                        .param("username", "laplume")
                        .param("email", "laplume@yahoo.fr")
                        .param("password", "123")
                        .param("retypePassword", "123"))
                .andExpect(model().attributeHasFieldErrorCode("signupDto", "email", "error.emailExists"))
                .andExpect(content().string(containsString("Email already exists!")))
                .andExpect(view().name("signup"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("newUserPasswordNotMatchError, should display error message for given not match retyped password")
    void newUserPasswordNotMatchError_ShouldReturnValidationPasswordNotMatchError_ForGivenNotMatchRetypedPassword() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/newUser")
                        .param("username", "maximus")
                        .param("email", "maximus@yahoo.fr")
                        .param("password", "12356")
                        .param("retypePassword", "123"))
                .andExpect(model().attributeHasFieldErrorCode("signupDto", "retypePassword", "error.passwordNotMatch"))
                .andExpect(content().string(containsString("Passwords doesn t match!")))
                .andExpect(view().name("signup"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }


    @Test
    @DisplayName("newUserSuccess, should redirect to login page for given correct input field")
    void newUserSuccess_ShouldRedirectToLoginPage_ForGivenCorrectInputField() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/newUser")
                        .param("username", "lucie")
                        .param("email", "lucie@yahoo.fr")
                        .param("password", "1234")
                        .param("retypePassword", "1234"))
                .andExpect(view().name("login"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("loginError should return login error message for given wrong email")
    void loginUserError_ShouldReturnUnauthenticated_ForGivenWrongEmail() throws Exception {
        // GIVEN
        // WHEN

        this.mvc.perform(MockMvcRequestBuilders.post("/loginUser")
                        .param("email", "emrode@yahoo.fr")
                        .param("password", "123"))
                .andExpect(content().string(containsString("Email or Password incorrect")))
                .andExpect(view().name("login"))
                .andExpect(status().isOk())
                .andDo(print());

        // THEN
    }

    @Test
    @DisplayName("loginError should return login error message for given wrong password")
    void loginUserError_ShouldReturnUnauthenticated_ForGivenWrongPassword() throws Exception {
        // GIVEN
        // WHEN

        this.mvc.perform(MockMvcRequestBuilders.post("/loginUser")
                        .param("email", "ludivine@yahoo.fr")
                        .param("password", "123678"))
                .andExpect(content().string(containsString("Email or Password incorrect")))
                .andExpect(view().name("login"))
                .andExpect(status().isOk())
                .andDo(print());

        // THEN
    }


    @Test
    @DisplayName("loginSuccess should redirect to myProfile page for given correct credentials")
    void loginUserSuccess_ShouldRedirectToMyProfilePage_ForGivenCorrectCredentials() throws Exception {
        // GIVEN

        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/loginUser")
                        .param("email", "francois@yahoo.fr")
                        .param("password", "123"))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/myProfile"))
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("editPassword, should display edit password page")
    @WithUserDetails("laplume@yahoo.fr")
    void editPassword_ShouldDisplayEditPasswordPage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/password"))
                .andExpect(view().name("user/edit-password"))
                .andDo(print())
                .andExpect(status().isOk());
        // THEN
    }

    @Test
    @DisplayName("editPasswordBlankInputFieldError, should display error message for given blank input field")
    @WithUserDetails("laplume@yahoo.fr")
    void editPasswordBlankInputFieldError_ShouldDisplayErrorMessage_ForGivenBlankInputField() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/password/edit")
                        .param("oldPassword", "")
                        .param("newPassword", "")
                        .param("retypedNewPassword", ""))
                .andExpect(model().attributeHasFieldErrors(
                        "passwordEditDto", "oldPassword", "newPassword", "retypedNewPassword"))
                .andExpect(view().name("user/edit-password"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("editPasswordIncorrectOldPasswordError, should display error message for given incorrect old password")
    @WithUserDetails(value = "laplume@yahoo.fr")
    void editPasswordIncorrectOldPasswordError_ShouldDisplayErrorMessage_ForGivenIncorrectOldPassword() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/password/edit")
                        .param("oldPassword", "123456")
                        .param("newPassword", "12")
                        .param("retypedNewPassword", "12"))
                .andExpect(model().attributeHasFieldErrors(
                        "passwordEditDto", "oldPassword"))
                .andExpect(content().string(containsString("Incorrect old password!")))
                .andExpect(view().name("user/edit-password"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("editPasswordNotMatchPassword, should display error message for given not match retyped new password")
    @WithUserDetails(value = "laplume@yahoo.fr")
    void editPasswordNotMatchPassword_ShouldDisplayErrorMessage_ForGivenNotMatchRetypedNewPassword() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/password/edit")
                        .param("oldPassword", "123")
                        .param("newPassword", "456")
                        .param("retypedNewPassword", "128"))
                .andExpect(model().attributeHasFieldErrors(
                        "passwordEditDto", "retypedNewPassword"))
                .andExpect(content().string(containsString("Password doesn t match!")))
                .andExpect(view().name("user/edit-password"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("editPasswordSuccess, should logout user")
    @WithUserDetails(value = "arnold@yahoo.fr")
    void editPasswordSuccess_ShouldLogoutUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/password/edit")
                        .param("oldPassword", "123")
                        .param("newPassword", "456")
                        .param("retypedNewPassword", "456"))
                .andExpect(view().name("redirect:/logout"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("addBankAccount, should display add account bank page")
    @WithUserDetails(value = "laplume@yahoo.fr")
    void addBankAccount_ShouldDisplayAddAccountBankPage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/bankAccount"))
                .andExpect(view().name("user/add-bank"))
                .andDo(print())
                .andExpect(status().isOk());
        // THEN
    }

    @Test
    @DisplayName("addBankAccountBlankInputFieldError, should display error message for given blank input field")
    @WithUserDetails(value = "laplume@yahoo.fr")
    void addBankAccountBlankInputFieldError_ShouldDisplayErrorMessage_ForGivenBlankInputField() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/bankAccount/add")
                        .param("bankAccountName", "")
                        .param("description", "")
                        .param("iban", ""))
                .andExpect(model().attributeHasFieldErrors(
                        "bankDto", "bankAccountName", "description", "iban"))
                .andExpect(view().name("user/add-bank"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("addBankAccountSuccess, should redirect to myProfile page")
    @WithUserDetails(value = "laplume@yahoo.fr")
    void addBankAccountSuccess_ShouldRedirectToMyProfilePage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/bankAccount/add")
                        .param("bankAccountName", "Boursorama")
                        .param("description", "la banque que les autres adorent")
                        .param("iban", "azerty"))
                .andExpect(view().name("redirect:/myProfile"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }


}