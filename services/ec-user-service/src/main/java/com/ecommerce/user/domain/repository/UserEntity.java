package com.ecommerce.user.domain.repository;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean validated = false;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "validate_date")
    private LocalDateTime validateDate;

    public UserEntity() {
    }

    public UserEntity(String email, String password, boolean validated, LocalDateTime createdDate) {
        this.email = email;
        this.password = password;
        this.validated = validated;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getValidateDate() {
        return validateDate;
    }

    public void setValidateDate(LocalDateTime validateDate) {
        this.validateDate = validateDate;
    }
}
