package com.gs;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.gs.event.LoanEvent;
import com.gs.model.Loan;
import com.gs.model.LoanStatus;
import com.gs.repository.ILoanRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ILoanRepository loanRepository;

    @MockitoBean
    private KafkaTemplate<String, LoanEvent> kafkaTemplate;

    @Test
    void approveLoan_shouldPublishKafkaEventAndUpdateLoan() throws Exception {
        String loanId = "loan123";
        Loan loan = new Loan();
        loan.setLoanId(loanId);
        loan.setCustomerId("cust123");
        loan.setLoanAmount(100000.0);
        loan.setStatus(LoanStatus.PENDING);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        mockMvc.perform(get("/loans/approve/{loanId}", loanId))
                .andExpect(status().isOk())
                .andExpect(content().string((org.hamcrest.Matchers.containsString("Loan approved"))));

        verify(loanRepository).save(any(Loan.class));
        verify(kafkaTemplate).send(eq("loan-status-events"), eq(loanId), any(LoanEvent.class));
    }
}
