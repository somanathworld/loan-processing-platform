package com.gs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPublicEndpoint() throws Exception {
        mockMvc.perform(get("/users/public"))
                .andExpect(status().isOk())
                .andExpect(content().string("Public endpoint"));
    }

    @Test   
    void testPublicStatusEndpoint() throws Exception {
        mockMvc.perform(get("/users/info")
                .header("X-User", "adminUser")
                .header("X-Roles", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Service is running"));
    }

    @Test
    void testSecureAdminEndpointWithRoleAdmin() throws Exception {
        mockMvc.perform(get("/users//health/admin")
                .header("X-User", "adminUser")
                .header("X-Roles", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Service is running"));
    }

    @Test
    void testSecureAdminEndpointWithRoleUserOnly() throws Exception {
        mockMvc.perform(get("/users/health/admin")
                .header("X-User", "normalUser")
                .header("X-Roles", "ROLE_USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testSecureEndpointWithoutHeaders() throws Exception {
        mockMvc.perform(get("/users//health/admin"))
                .andExpect(status().isForbidden());
    }
}
