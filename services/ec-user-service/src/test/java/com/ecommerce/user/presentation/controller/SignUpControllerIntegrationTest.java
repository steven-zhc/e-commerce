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

    @Test
    void signUp_WithExistingUser_ShouldReturnConflict() {
        // Arrange
        String email = "duplicate" + System.currentTimeMillis() + "@example.com";
        SignUpRequest request = new SignUpRequest(email, "password123");

        // First signup
        restTemplate.postForEntity("/api/user/", request, SignUpResponse.class);

        // Act - Try to signup again with same email
        ResponseEntity<SignUpResponse> response = restTemplate.postForEntity(
            "/api/user/",
            request,
            SignUpResponse.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void signUp_WithInvalidEmail_ShouldReturnBadRequest() {
        // Arrange
        SignUpRequest request = new SignUpRequest("invalid-email", "password123");

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/user/",
            request,
            String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void signUp_WithShortPassword_ShouldReturnBadRequest() {
        // Arrange
        SignUpRequest request = new SignUpRequest("test@example.com", "short");

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/user/",
            request,
            String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void signUp_WithMissingEmail_ShouldReturnBadRequest() {
        // Arrange
        SignUpRequest request = new SignUpRequest(null, "password123");

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/user/",
            request,
            String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void signUp_WithMissingPassword_ShouldReturnBadRequest() {
        // Arrange
        SignUpRequest request = new SignUpRequest("test@example.com", null);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/user/",
            request,
            String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
