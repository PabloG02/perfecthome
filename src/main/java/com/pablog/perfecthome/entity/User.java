package com.pablog.perfecthome.entity;

import com.pablog.perfecthome.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
public abstract class User {
    @Id
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 30, message = "Username cannot exceed 30 characters")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role cannot be null")
    private UserRole role;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @Column(nullable = false)
    @NotNull(message = "isActive cannot be null")
    private Boolean isActive;

    public User() {
    }

    public User(String username, String password, String email, String phone, UserRole role, LocalDateTime registrationDate, Boolean isActive) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.registrationDate = registrationDate;
        this.isActive = isActive;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(phone, user.phone) && role == user.role && Objects.equals(registrationDate, user.registrationDate) && Objects.equals(isActive, user.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, phone, role, registrationDate, isActive);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", registrationDate=" + registrationDate +
                ", isActive=" + isActive +
                '}';
    }
}
