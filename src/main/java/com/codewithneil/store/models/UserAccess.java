package com.codewithneil.store.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_access")
public class UserAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "access_id")
    private SystemAccess access;

    // getters & setters
    public SystemAccess getAccess() {
        return access;
    }
}