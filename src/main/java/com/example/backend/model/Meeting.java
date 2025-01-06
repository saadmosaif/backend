package com.example.backend.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String hostUsername;

    private String title;

    // Add more fields as needed (e.g., participant list)

    // Getters and setters
}
