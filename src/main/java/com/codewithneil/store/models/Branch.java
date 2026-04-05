package com.codewithneil.store.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    public String getName() {
        return name;
    }
}