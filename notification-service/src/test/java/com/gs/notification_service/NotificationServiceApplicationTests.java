package com.gs.notification_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.gs.service.EmailService;

@SpringBootTest
class NotificationServiceApplicationTests {

	 @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService mailService;

	@Test
	void contextLoads() {
		 // Arrange
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        mailService.sendLoanStatusEmail("test@example.com", "USER123", "LOAN456", "APPROVED");

        // Assert
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("test@example.com", sentMessage.getTo()[0]);
        assertEquals("Your Loan Application Status", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("USER123"));
        assertTrue(sentMessage.getText().contains("LOAN456"));
        assertTrue(sentMessage.getText().contains("APPROVED"));
	}

}
