package com.example.backend.controller;

import com.example.backend.model.Meeting;
import com.example.backend.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    @Autowired
    private MeetingRepository meetingRepository;

    @PostMapping("/create")
    public Meeting createMeeting(@RequestBody Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    @GetMapping("/{id}")
    public Meeting getMeeting(@PathVariable UUID id) {
        return meetingRepository.findById(id).orElse(null);
    }
}
