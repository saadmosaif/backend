package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDateTime createdAt;

    @Column(name = "host_username", nullable = false)
    private String host;

    private String videoCallUrl; // The link to join the video call
    private boolean isVideoEnabled; // Indicates if video is enabled for this meeting


    public Meeting() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    // Getters and Setters for new fields
    public String getVideoCallUrl() {
        return videoCallUrl;
    }

    public void setVideoCallUrl(String videoCallUrl) {
        this.videoCallUrl = videoCallUrl;
    }

    public boolean isVideoEnabled() {
        return isVideoEnabled;
    }

    public void setVideoEnabled(boolean videoEnabled) {
        isVideoEnabled = videoEnabled;
    }
}
