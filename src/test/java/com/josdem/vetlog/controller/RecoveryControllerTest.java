package com.josdem.vetlog.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class RecoveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("getting email to change password")
    void shouldRequestEmailToChangePassword(TestInfo testInfo) throws Exception {
        log.info("Running: {}", testInfo.getDisplayName());
        mockMvc.perform(get("/recovery/password"))
                .andExpect(status().isOk())
                .andExpect(view().name("recovery/recoveryPassword"));
    }

    @Test
    @DisplayName("showing change password forms")
    void shouldShowChangePasswordForms(TestInfo testInfo) throws Exception {
        log.info("Running: {}", testInfo.getDisplayName());
        mockMvc.perform(get("/recovery/forgot/token"))
                .andExpect(status().isOk())
                .andExpect(view().name("recovery/changePassword"));
    }

    @Test
    @DisplayName("user token not found")
    void shouldNotFindUserToken(TestInfo testInfo) throws Exception {
        log.info("Running: {}", testInfo.getDisplayName());
        mockMvc.perform(get("/recovery/activate/token"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    @DisplayName("not generating token for changing password due to user not found")
    void shouldNotGenerateTokenToChangePassword(TestInfo testInfo) throws Exception {
        log.info("Running: {}", testInfo.getDisplayName());
        mockMvc.perform(post("/recovery/password").with(csrf()).param("email", "contact@josdem.io"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    @DisplayName("not changing password due to token not found")
    void shouldNotChangePassword(TestInfo testInfo) throws Exception {
        log.info("Running: {}", testInfo.getDisplayName());
        mockMvc.perform(post("/recovery/change")
                        .with(csrf())
                        .param("token", "18c58288-cb57-46dc-b14f-e3ebc2d9b8ce")
                        .param("password", "12345678")
                        .param("passwordConfirmation", "12345678"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
}
