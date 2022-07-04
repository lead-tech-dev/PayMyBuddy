package com.mjtech.mybuddy.web.controller;

import com.mjtech.mybuddy.web.AbstractTest;
import com.mjtech.mybuddy.web.service.ContactService;
import com.mjtech.mybuddy.web.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminControllerTest extends AbstractTest {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("getOneUserPage, should display admin myProfile page")
    @WithUserDetails("maxitella@yahoo.fr")
    void getOneUserPage_ShouldDisplayAdminMyProfilePage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile"))
                .andExpect(view().name("myProfile"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("userOperation, should display user operation list page for given user id")
    @WithUserDetails("maxitella@yahoo.fr")
    void userOperation_ShouldDisplayUserOperationListPage_ForGivenUserId() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/admin/user/2"))
                .andExpect(content().string(containsString("laplume")))
                .andExpect(view().name("user-details-operation"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("getOneCouponPage, should display manage coupon page")
    @WithUserDetails("maxitella@yahoo.fr")
    void getOneCouponPage_ShouldDisplayManageCouponPage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/admin/coupon"))
                .andExpect(view().name("coupon"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }


    @Test
    @DisplayName("generate, should redirect to coupon generate page")
    @WithUserDetails("maxitella@yahoo.fr")
    void generate_ShouldRedirectToCouponGeneratePage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.post("/myProfile/admin/coupon/generate")
                        .param("nbrCoupon", "10")
                        .param("price", "90"))
                .andExpect(view().name("redirect:/myProfile/admin/coupon"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("deleteCoupon, should redirect to coupon manage current page for given coupon id and current page")
    @WithUserDetails("maxitella@yahoo.fr")
    void deleteCoupon_ShouldRedirectToCouponManagePageCurrentPage_ForGivenCouponIdAndCurrentPage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/admin/coupon/5/1"))
                .andExpect(view().name("redirect:/myProfile/admin/coupon/page/" + 1))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("getOneContactPage, should display contact page with message list for no empty message list")
    @WithUserDetails("maxitella@yahoo.fr")
    void getOneContactPage_ShouldDisplayContactPageWithMessageList_ForNoEmptyMessageList() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/admin/messages"))
                .andExpect(view().name("messages"))
                .andExpect(content().string(containsString("Firstname")))
                .andExpect(content().string(containsString("Email")))
                .andExpect(content().string(containsString("Subject")))
                .andExpect(content().string(containsString("Action")))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }


    @Test
    @DisplayName("deleteContact, should redirect to contact manage page for given contact id and current page")
    @WithUserDetails("maxitella@yahoo.fr")
    void deleteContact_ShouldDeleteOneAndRedirectToContactManagePage_ForGivenContactIdAndCurrentPage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/admin/messages/remove/1/1"))
                .andExpect(view().name("redirect:/myProfile/admin/messages/page/1"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }

    @Test
    @DisplayName("messageDetailsError, should redirect to contact manage page for given wrong contact id")
    @WithUserDetails("maxitella@yahoo.fr")
    void messageDetailsError_ShouldRedirectToContactManage_ForGivenWrongContactId() throws Exception {
        // GIVEN

        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/admin/messages/10000000"))
                .andExpect(view().name("redirect:/myProfile/admin/messages"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }


    @Test
    @DisplayName("messageDetails, should display contact detail page for given contact correct id")
    @WithUserDetails("maxitella@yahoo.fr")
    void messageDetailsSuccess_ShouldDisplayContactDetailPage_ForGivenContactCorrectId() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/admin/messages/2"))
                .andExpect(view().name("message-details"))
                .andExpect(status().isOk())
                .andDo(print());
        // THEN
    }


    @Test
    @DisplayName("deleteUser, should delete user for given id, current page and redirect to myProfile page")
    @WithUserDetails("maxitella@yahoo.fr")
    void deleteUser_ShouldDeleteUser_ForGivenIdAndRedirectToMyProfilePage() throws Exception {
        // GIVEN
        // WHEN
        this.mvc.perform(MockMvcRequestBuilders.get("/myProfile/admin/user/remove/5/1"))
                .andExpect(view().name("redirect:/myProfile/page/1"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // THEN
    }
}