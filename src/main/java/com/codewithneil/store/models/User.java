package com.codewithneil.store.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String phone;

    // 🔹 NAME FIELDS
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    // 🔹 ROLE RELATION (FK)
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // 🔹 BRANCH RELATION (FK)
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    // 🔹 ACCESS RELATION (permissions)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserAccess> userAccesses;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // =====================
    // GETTERS
    // =====================

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public Branch getBranch() {
        return branch;
    }

    public List<UserAccess> getUserAccesses() {
        return userAccesses;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // =====================
    // SETTERS
    // =====================

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public void setUserAccesses(List<UserAccess> userAccesses) {
        this.userAccesses = userAccesses;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}