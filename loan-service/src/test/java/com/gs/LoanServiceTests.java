package com.gs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gs.clients.UserServiceClient;
import com.gs.service.ILoanMgmtService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.invocation.ListenableFutureReturnValueHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.concurrent.ListenableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceClient userServiceClient;

    @MockitoBean
    private ILoanMgmtService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoanApply() throws Exception {
        
        String json = "{\"customerId\":\"cust123\",\"amount\":50000,\"termInMonths\":24,\"purpose\":\"Home\"}";
        System.out.println("Testing Loan Apply with JSON: " + objectMapper.writeValueAsString(json));
        mockMvc.perform(post("/loans/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Loan application submitted")));
    }
}
