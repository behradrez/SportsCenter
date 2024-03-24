package ca.mcgill.ecse321.Sport.Center.Application.ECSE321.controller;

import ca.mcgill.ecse321.Sport.Center.Application.ECSE321.dto.SessionResponseDTO;
import ca.mcgill.ecse321.Sport.Center.Application.ECSE321.model.*;
import ca.mcgill.ecse321.Sport.Center.Application.ECSE321.service.SchedulingService;
import ca.mcgill.ecse321.Sport.Center.Application.ECSE321.dto.SessionDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ScheduleController {
    @Autowired
    private SchedulingService service;

    @PostMapping("/sessions")
    public ResponseEntity<?> createSession(@RequestBody SessionDTO request) {
        Session newSession = null;
        try {
            newSession = service.createSession(request.getLength(), request.getStartTime(), request.getEndTime(), request.getDate(), request.getIsRepeating(), request.getMaxParticipants(), request.getClassType(), null);
            newSession.setInstructor(request.getInstructor());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new SessionDTO(newSession), HttpStatus.CREATED);
    }

    @PutMapping("/sessions/{id}")
    public ResponseEntity<?> updateSession (@PathVariable int id, @RequestBody SessionDTO request) {
        SessionDTO newSession = null;
        try {
            newSession = service.updateSession(id, request.getLength(), request.getStartTime(), request.getEndTime(), request.getDate(), request.getIsRepeating(), request.getMaxParticipants(), request.getClassType(), request.getInstructor());
            return new ResponseEntity<>(newSession,HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/sessions/{id}") 
    public ResponseEntity<SessionDTO> deleteSession (@PathVariable int id) {
        service.deleteSession(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<?> findSessionById(@PathVariable int id) {
        Session session = service.findSessionById(id);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new SessionDTO(session));
    }

    @GetMapping("/sessions")
    public List<SessionDTO> findAllSessions() {
        List<SessionDTO> dtos = new ArrayList<>();
        for (Session s : service.findAllSessions()) {
            dtos.add(new SessionDTO(s));
        }
        return dtos;
    }
}

