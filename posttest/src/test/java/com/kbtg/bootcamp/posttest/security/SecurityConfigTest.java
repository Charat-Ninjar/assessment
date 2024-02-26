package com.kbtg.bootcamp.posttest.security;

import com.kbtg.bootcamp.posttest.admin.AdminController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@EnableMethodSecurity(prePostEnabled = true)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminController adminController;

    @Test
    @WithAnonymousUser
    public void testAdminAccess() throws Exception {

        String jsonContent = "{ \"ticket\": \"111111\", \"price\": 80, \"amount\": 1 }";

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUnauthorizedAccess() throws Exception {

        String jsonContent = "{ \"ticket\": \"111111\", \"price\": 80, \"amount\": 1 }";

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
