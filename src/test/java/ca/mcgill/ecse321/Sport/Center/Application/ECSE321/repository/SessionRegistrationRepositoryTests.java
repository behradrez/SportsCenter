package ca.mcgill.ecse321.Sport.Center.Application.ECSE321.repository;

import ca.mcgill.ecse321.Sport.Center.Application.ECSE321.dao.*;
import ca.mcgill.ecse321.Sport.Center.Application.ECSE321.model.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SessionRegistrationRepositoryTests {

    @Autowired
    private SessionRegistrationRepository registrationRepo;
    @Autowired
    private SessionRepository sessionRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private InstructorRepository instructorRepo;
    @Autowired
    private ClassTypeRepository classTypeRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        registrationRepo.deleteAll();
        sessionRepo.deleteAll();
        customerRepo.deleteAll();
        instructorRepo.deleteAll();
        classTypeRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadRegistration() {
        // Create Times
        LocalTime localStartTime = LocalTime.of(11, 0, 0); // 08:30:00
        LocalTime localEndTime = LocalTime.of(12, 0, 0);   // 12:00:00

        //Create Customer
        int customerId = 1;
        Customer customer = new Customer(customerId);
        customer = customerRepo.save(customer);

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
        Session sessionRegistration = new Session(sessionId, length, startTime, endTime, date,
                isRepeating, maxParticipants, exampleClassType, instructor);
        sessionRegistration = sessionRepo.save(sessionRegistration);

        // Create registration
        int regId = 1;
        SessionRegistration reg = new SessionRegistration(regId, sessionRegistration, customer);

        // Save in database
        reg = registrationRepo.save(reg);

        // Read back from database
        regId = reg.getId();
        SessionRegistration regFromDB = registrationRepo.getSessionRegistrationById(regId);

        //Base Registration Assertions
        assertNotNull(regFromDB);
        assertEquals(regId, regFromDB.getId());
        //Customer Assertions
        Customer customerFromDB = regFromDB.getCustomer();
        assertNotNull(customerFromDB);
        assertEquals(customer.getId(), customerFromDB.getId());
        //Session Assertions
        Session sessionRegistrationFromDB = regFromDB.getSession();
        assertNotNull(sessionRegistrationFromDB);
        assertEquals(sessionRegistration.getClassType().getClassType(), sessionRegistrationFromDB.getClassType().getClassType());
        assertEquals(sessionRegistration.getDate(), sessionRegistrationFromDB.getDate());
        assertEquals(sessionRegistration.getEndTime(), sessionRegistrationFromDB.getEndTime());
        assertEquals(sessionRegistration.getId(), sessionRegistrationFromDB.getId());
        assertEquals(sessionRegistration.getInstructor().getId(), sessionRegistrationFromDB.getInstructor().getId());
        assertEquals(sessionRegistration.getIsRepeating(), sessionRegistrationFromDB.getIsRepeating());
        assertEquals(sessionRegistration.getLength(), sessionRegistrationFromDB.getLength());
        assertEquals(sessionRegistration.getMaxParticipants(), sessionRegistrationFromDB.getMaxParticipants());
        assertEquals(sessionRegistration.getStartTime(), sessionRegistrationFromDB.getStartTime());
        //Instructor Assertions
        ClassType typeFromDB = sessionRegistrationFromDB.getClassType();
        assertNotNull(typeFromDB);
        assertEquals(sessionRegistration.getClassType().getClassType(), typeFromDB.getClassType());

    }

}
