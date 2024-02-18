package ca.mcgill.ecse321.Sport.Center.Application.ECSE321.repository;

import ca.mcgill.ecse321.Sport.Center.Application.ECSE321.dao.ClassTypeRepository;
import ca.mcgill.ecse321.Sport.Center.Application.ECSE321.dao.InstructorRepository;
import ca.mcgill.ecse321.Sport.Center.Application.ECSE321.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.Sport.Center.Application.ECSE321.dao.SessionRepository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SessionRepositoryTests {

    @Autowired
    private InstructorRepository instructorRepo;
    @Autowired
    private ClassTypeRepository classTypeRepo;
    @Autowired
    private SessionRepository sessionRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        sessionRepo.deleteAll();
        instructorRepo.deleteAll();
        classTypeRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadSession() {
        // Create Times
        LocalTime localStartTime = LocalTime.of(11, 0, 0); // 08:30:00
        LocalTime localEndTime = LocalTime.of(12, 0, 0);   // 12:00:00

        // Create instructor
        int instructorId = 1;
        Instructor instructor = new Instructor(instructorId);
        instructor = instructorRepo.save(instructor);

        // Create class type
        ClassType exampleClassType = new ClassType("exampleClassType");
        exampleClassType = classTypeRepo.save(exampleClassType);

        // Create session
        int sessionId = 1;
        int length = 60;
        Time startTime = Time.valueOf(localStartTime);
        Time endTime = Time.valueOf(localEndTime);
        Date date = Date.valueOf(LocalDate.of(2024, 2, 18));
        boolean isRepeating = true;
        int maxParticipants = 50;
        Session yogaSession = new Session(sessionId, length, startTime, endTime, date,
                isRepeating, maxParticipants, exampleClassType, instructor);

        // Save in database
        yogaSession = sessionRepo.save(yogaSession);
        
        // Read back from database
        sessionId = yogaSession.getId();
        Session sessionFromDB = sessionRepo.getSessionById(sessionId);

        // Assertions
        assertNotNull(sessionFromDB);
        assertEquals(yogaSession.getId(), sessionFromDB.getId());
        assertEquals(yogaSession.getLength(), sessionFromDB.getLength());
        assertEquals(yogaSession.getStartTime(), sessionFromDB.getStartTime());
        assertEquals(yogaSession.getEndTime(), sessionFromDB.getEndTime());
        assertEquals(yogaSession.getDate(), sessionFromDB.getDate());
        assertEquals(yogaSession.getIsRepeating(), sessionFromDB.getIsRepeating());
        assertEquals(yogaSession.getMaxParticipants(), sessionFromDB.getMaxParticipants());
        assertEquals(yogaSession.getClassType().getClassType(), sessionFromDB.getClassType().getClassType());
        assertNotNull(yogaSession.getInstructor());
        assertEquals(yogaSession.getInstructor().getId(), sessionFromDB.getInstructor().getId());
    }

}