package com.pablog.perfecthome.entity;

import com.pablog.perfecthome.enums.UserRole;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Client extends User {

    public Client() {
        super();
        this.setRole(UserRole.CLIENT);
    }

    public Client(String username, String password, String email, String phone, LocalDateTime registrationDate, Boolean isActive) {
        super(username, password, email, phone, UserRole.CLIENT, registrationDate, isActive);
    }

    @Override
    public String toString() {
        return "Client{} " + super.toString();
    }
}
