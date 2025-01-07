package com.example.backend.controller;
import com.example.backend.util.JwtUtil;
import com.example.backend.model.Meeting;
import com.example.backend.repository.MeetingRepository;
import com.example.backend.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private JwtUtil jwtUtil; // Inject JwtUtil instance
    @Autowired
    private MeetingRepository meetingRepository;

    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @PostMapping
    public ResponseEntity<Meeting> createMeeting(@RequestBody Meeting meeting) {
        // Get the username of the currently authenticated user
        String username = getCurrentUsername();
        meeting.setHost(username); // Set the host as the current user's username

        Meeting savedMeeting = meetingService.saveMeeting(meeting);
        return ResponseEntity.ok(savedMeeting);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting(@PathVariable Long id, @RequestBody Meeting updatedMeeting, @RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.substring(7)); // Call method on instance
        Meeting existingMeeting = meetingService.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        if (!existingMeeting.getHost().equals(username)) {
            return ResponseEntity.status(403).body("You are not authorized to edit this meeting.");
        }

        existingMeeting.setTitle(updatedMeeting.getTitle());
        existingMeeting.setDescription(updatedMeeting.getDescription());
        meetingService.save(existingMeeting);

        return ResponseEntity.ok(existingMeeting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id) {
        meetingService.deleteMeeting(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Meeting deleted successfully.");
        return ResponseEntity.ok(response);
    }





}
