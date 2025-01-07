package com.example.backend.service;

import com.example.backend.model.Meeting;
import com.example.backend.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    public Optional<Meeting> findById(Long id) {
        return meetingRepository.findById(id);
    }


    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    public Meeting findMeetingById(Long id) {
        // Ensure the meeting exists
        return meetingRepository.findById(id).orElseThrow(() -> new RuntimeException("Meeting not found"));
    }

    public Meeting save(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public void validateOwnership(Long meetingId, String username) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        if (!meeting.getHost().equals(username)) {
            throw new RuntimeException("Unauthorized to modify this meeting");
        }
    }
    public Meeting saveMeeting(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public Meeting updateMeeting(Long id, Meeting meeting) {
        Meeting existingMeeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        // Update fields
        existingMeeting.setTitle(meeting.getTitle());
        existingMeeting.setDescription(meeting.getDescription());
        // Preserve existing createdAt and host fields
        return meetingRepository.save(existingMeeting);
    }

    public void deleteMeeting(Long id) {
        meetingRepository.deleteById(id);
    }
}
