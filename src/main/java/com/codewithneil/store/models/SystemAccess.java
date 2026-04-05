package com.codewithneil.store.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "system_access")
public class SystemAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String code;

    private String description;

    // getters
    public String getCode() {
        return code;
    }
}