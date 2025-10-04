package com.ecommerce.user.presentation.controller;

import com.ecommerce.user.presentation.dto.SignUpRequest;
import com.ecommerce.user.presentation.dto.SignUpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SignUpControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void signUp_WithValidRequest_ShouldReturnCreated() {
        // Arrange
        String uniqueEmail = "integration" + System.currentTimeMillis() + "@example.com";
        SignUpRequest request = new SignUpRequest(uniqueEmail, "password123");

        // Act
        ResponseEntity<SignUpResponse> response = restTemplate.postForEntity(
            "/api/user/",
            request,
            SignUpResponse.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(uniqueEmail);
        assertThat(response.getBody().getCreatedAt()).isNotNull();
    }
}
