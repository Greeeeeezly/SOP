package com.example.defaultconsumer.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private boolean priority;

    @Column(nullable = false)
    private boolean isActive;  // Поле isActive только для приоритетных пользователей

    public User() {}

    public User(String name, String email, boolean priority, boolean isActive) {
        this.name = name;
        this.email = email;
        this.priority = priority;
        this.isActive = priority ? isActive : false;  // Если не приоритетный, isActive всегда false
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public boolean isActive() {
        return priority ? isActive : false;  // Только для приоритетных пользователей
    }

    public void setActive(boolean isActive) {
        if (priority) {
            this.isActive = isActive;
        } else {
            this.isActive = false;  // Не может быть true для обычных пользователей
        }
    }
}
