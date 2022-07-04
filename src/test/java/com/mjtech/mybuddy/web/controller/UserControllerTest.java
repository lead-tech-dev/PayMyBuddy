package com.mjtech.mybuddy.web.controller;

import com.mjtech.mybuddy.web.AbstractTest;
import com.mjtech.mybuddy.web.service.CouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends AbstractTest {
    @Autowired
    private CouponService couponService;

    @Test
    @DisplayName("recharge, should display user recharge page")
    @WithUserDetails(value = "arnold@yahoo.fr")
    void recharge_ShouldDisplayUserRechargePage() throws Exception {
        // GIVEN

        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/recharge"))
                .andExpect(view().name("user/recharge"))
                .andExpect(status().isOk())
                .andDo(print());

        // THEN
    }

    @Test
    @DisplayName("rechargeBlankInputFieldError, should display error message for blank input field")
    @WithUserDetails("arnold@yahoo.fr")
    void rechargeBlankInputFieldError_ShouldDisplayErrorMessage_ForGivenBlankInputField() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/recharge/add")
                        .param("couponId", ""))
                .andExpect(model().attributeHasFieldErrors(
                        "couponRechargeDto", "couponId"))
                .andExpect(content().string(containsString("must not be blank")))
                .andExpect(view().name("user/recharge"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("rechargeStringValueError, should redirect to user recharge page for given string input value")
    @WithUserDetails("arnold@yahoo.fr")
    void rechargeStringValueError_ShouldRedirectToUserRechargePage_ForGivenInputStringValue() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/recharge/add")
                        .param("couponId", "A12@-"))
                .andExpect(model().attributeHasFieldErrors(
                        "couponRechargeDto", "couponId"))
                .andExpect(content().string(containsString("Please enter a correct coupon code!")))
                .andExpect(view().name("user/recharge"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("rechargeNotExistCouponError, should display error message for given not exist coupon")
    @WithUserDetails("arnold@yahoo.fr")
    void rechargeNotExistCouponError_ShouldDisplayErrorMessage_ForGivenNotExistCoupon() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/recharge/add")
                        .param("couponId", "100000000"))
                .andExpect(model().attributeHasFieldErrors(
                        "couponRechargeDto", "couponId"))
                .andExpect(content().string(containsString("Coupon code not exists!")))
                .andExpect(view().name("user/recharge"))
                .andExpect(status().isOk())
                .andDo(print());

        // THEN
    }

    @Test
    @DisplayName("rechargeCouponInactiveError, should display error message for given coupon inactive")
    @WithUserDetails("arnold@yahoo.fr")
    void rechargeCouponInactiveError_ShouldDisplayErrorMessage_ForGivenCouponInactive() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/recharge/add")
                        .param("couponId", "2"))
                .andExpect(model().attributeHasFieldErrors(
                        "couponRechargeDto", "couponId"))
                .andExpect(content().string(containsString("Inactive coupon code!")))
                .andExpect(view().name("user/recharge"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("rechargeCouponExpiredError, should display error message for given coupon expired")
    @WithUserDetails("arnold@yahoo.fr")
    void rechargeCouponExpiredError_ShouldDisplayErrorMessage_ForGivenCouponExpired() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/recharge/add")
                        .param("couponId", "3"))
                .andExpect(model().attributeHasFieldErrors(
                        "couponRechargeDto", "couponId"))
                .andExpect(content().string(containsString("Expired coupon code!")))
                .andExpect(view().name("user/recharge"))
                .andExpect(status().isOk())
                .andDo(print());

        // THEN
    }

    @Test
    @DisplayName("rechargeSuccess, should redirect to myProfile page for no error")
    @WithUserDetails("arnold@yahoo.fr")
    void rechargeSuccess_ShouldRedirectToMyProfilePage_ForNoError() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/recharge/add")
                        .param("couponId", "1"))
                .andExpect(view().name("redirect:/myProfile"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("getOneUserPage, should display add-connection page")
    @WithUserDetails("arnold@yahoo.fr")
    void getOneUserPage_ShouldDisplayAddConnectionPage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/add-connection"))
                .andExpect(view().name("add-connection"))
                .andExpect(status().isOk())
                .andDo(print());

        // THEN
    }

    @Test
    @DisplayName("addConnectionReceiverNotFoundError, should redirect to add connection page for given and auth user")
    @WithUserDetails("arnold@yahoo.fr")
    void addConnectionReceiverNotFoundError_ShouldRedirectToAddConnectionPage_ForGivenUserAndAuthUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/add-connection/add/10000000"))
                .andExpect(view().name("redirect:/myProfile/user/add-connection"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("addConnectionExistConnectionError, should redirect to add connection page for given and auth user")
    @WithUserDetails("arnold@yahoo.fr")
    void addConnectionExistConnectionError_ShouldRedirectToAddConnectionPage_ForGivenUserAndAuthUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/add-connection/add/1"))
                .andExpect(view().name("redirect:/myProfile/user/add-connection"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        // THEN
    }

    @Test
    @DisplayName("addConnectionSuccess, should redirect to add connection page for given and auth user")
    @WithUserDetails("arnold@yahoo.fr")
    void addConnectionSuccess_ShouldRedirectToAddConnectionPage_ForGivenUserAndAuthUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/add-connection/add/7"))
                .andExpect(view().name("redirect:/myProfile/user/add-connection"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("acceptConnectionNotExistAcceptedConnectionUser, should redirect to myProfile page  for given and auth user")
    @WithUserDetails("arnold@yahoo.fr")
    void acceptConnectionNotExistAcceptedConnectionUser_ShouldRedirectToMyProfilePage_ForGivenUserAndAuthUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/add-connection/accept/500"))
                .andExpect(view().name("redirect:/myProfile"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        // THEN
    }

    @Test
    @DisplayName("acceptConnectionNotExistConnectionError, should redirect to myProfile page  for given and auth user")
    @WithUserDetails("arnold@yahoo.fr")
    void acceptConnectionNotExistConnectionError_ShouldRedirectToMyProfilePage_ForGivenUserAndAuthUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/add-connection/accept/5"))
                .andExpect(view().name("redirect:/myProfile"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        // THEN
    }

    @Test
    @DisplayName("acceptConnectionSuccess, should redirect to myProfile page")
    @WithUserDetails("arnold@yahoo.fr")
    void acceptConnectionSuccess_ShouldRedirectToMyProfilePage_ForGivenUserAndAuthUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/add-connection/accept/1"))
                .andExpect(view().name("redirect:/myProfile"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("deleteConnectionNotExistConnectionError, should redirect to myProfile page  for given and auth user id")
    @WithUserDetails("arnold@yahoo.fr")
    void deleteConnection_ShouldRedirectToMyProfilePage_ForGivenNotExistRemovedConnectionUserId() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/add-connection/remove/500"))
                .andExpect(view().name("redirect:/myProfile"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("deleteConnectionNotExistConnectionError, should redirect to myProfile page  for given and auth user id")
    @WithUserDetails("arnold@yahoo.fr")
    void deleteConnection_ShouldRedirectToMyProfilePage_ForGivenNotExistConnection() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/add-connection/remove/5"))
                .andExpect(view().name("redirect:/myProfile"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("deleteConnectionSuccess, should redirect to myProfile page  for given and auth user id")
    @WithUserDetails("arnold@yahoo.fr")
    void deleteConnection_ShouldRedirectToMyProfilePage_ForGivenCorrectConnectionUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/add-connection/remove/1"))
                .andExpect(view().name("redirect:/myProfile"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("getOneTransferPage, should display user transfer page")
    @WithUserDetails("arnold@yahoo.fr")
    void getOneTransferPage_ShouldDisplayUserTransferPage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/user/transfer"))
                .andExpect(view().name("transfer"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }


    @Test
    @DisplayName("addTransferNotExistConnectionError, should redirect to transfer page for given user and auth user")
    @WithUserDetails("arnold@yahoo.fr")
    void addTransferNotExistSenderError_ShouldRedirectToTransferPage_ForGivenUserAndAuthUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/transfer/add")
                        .param("transfer", "99")
                        .param("description", "Trip money")
                        .param("amount", "60"))

                .andExpect(view().name("redirect:/myProfile/user/transfer"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("addTransferNotExistConnectionError, should redirect to transfer page for given user and auth user")
    @WithUserDetails("arnold@yahoo.fr")
    void addTransferNotExistConnectionError_ShouldRedirectToTransferPage_ForGivenUserAndAuthUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/transfer/add")
                        .param("transfer", "7")
                        .param("description", "Trip money")
                        .param("amount", "60"))

                .andExpect(view().name("redirect:/myProfile/user/transfer"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("addTransferBalanceLimitError, should redirect to transfer page for given user and auth user")
    @WithUserDetails("arnold@yahoo.fr")
    void addTransferBalanceLimitError_ShouldRedirectToTransferPage_ForGivenUserAndAuthUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/transfer/add")
                        .param("transfer", "1")
                        .param("description", "Trip money")
                        .param("amount", "660"))

                .andExpect(view().name("redirect:/myProfile/user/transfer"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        // THEN
    }

    @Test
    @DisplayName("addTransferSuccess, should redirect to transfer page for given user and auth user")
    @WithUserDetails("laplume@yahoo.fr")
    void addTransferSuccess_ShouldRedirectToTransferPage_ForGivenUserAndAuthUser() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/transfer/add")
                        .param("transfer", "6")
                        .param("description", "Trip money")
                        .param("amount", "60"))

                .andExpect(view().name("redirect:/myProfile/user/transfer"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("bankTransferBalanceLimitError, should redirect to transfer page for given auth user and amount")
    @WithUserDetails("cartman@yahoo.fr")
    void bankTransferBalanceLimitError_ShouldRedirectToTransferPage_ForGivenAuthUserAndAmount() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/transfer/bank")
                        .param("amount", "600"))

                .andExpect(view().name("redirect:/myProfile/user/transfer"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("bankTransferSuccess, should redirect to transfer page for given auth user and amount")
    @WithUserDetails("ludivine@yahoo.fr")
    void bankTransferSuccess_ShouldRedirectToTransferPage_ForGivenAuthUserAndAmount() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/user/transfer/bank")
                        .param("amount", "10"))

                .andExpect(view().name("redirect:/myProfile/user/transfer"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }
}