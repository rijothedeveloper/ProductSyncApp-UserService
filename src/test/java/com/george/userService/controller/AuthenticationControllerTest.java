//package com.george.userService.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.george.userService.config.JwtAuthenticationFilter;
//import com.george.userService.dto.EmailVerificationResponse;
//import com.george.userService.services.AuthenticationService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.access.SecurityConfig;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
////@WebMvcTest(AuthenticationController.class)
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
////@WebMvcTest(AuthenticationController.class)
////@ActiveProfiles("test")
//class AuthenticationControllerTest {
//
//
//    /*
//     * We can @Autowire MockMvc because the WebApplicationContext provides an
//     * instance/bean for us
//     */
//    @Autowired
//    MockMvc mockMvc;
//
//    /*
//     * Jackson mapper for Object -> JSON conversion
//     */
//    @Autowired
//    ObjectMapper mapper;
//
//    /*
//     * We use @MockBean because the WebApplicationContext does not provide
//     * any @Component, @Service or @Repository beans instance/bean of this service
//     * in its context. It only loads the beans solely required for testing the
//     * controller.
//     */
//    @MockBean
//    AuthenticationService authenticationService;
//
//    @Test
//    void getStatus() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/status"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void veifyUserEmail() throws Exception {
//        Mockito.when(authenticationService.verifyRegisterationEmail("token")).thenReturn(new EmailVerificationResponse("verified"));
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/verify_user_email?token=token"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{'message':'verified'}"));
//    }
////
////    @Test
////    void signup() {
////    }
////
////    @Test
////    void signin() {
////    }
////
////    @Test
////    void refresh() {
////    }
////
////    @Test
////    void handleValidationExceptions() {
////    }
//}