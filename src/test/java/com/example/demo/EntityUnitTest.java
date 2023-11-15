package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    private Doctor d1;

    private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;

    @BeforeEach
    void setup() {
        p1 = new Patient();
        p1.setFirstName("Juan");
        p1.setLastName("Ruiz");
        p1.setAge(34);
        p1.setEmail("j.ruiz@hospital.accwe");

        d1 = new Doctor();
        d1.setFirstName("Julia");
        d1.setLastName("Gomez");
        d1.setAge(29);
        d1.setEmail("j.gomez@hospital.accwe");

        r1 = new Room("Dermotology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt1 = LocalDateTime.parse("19:30 30/03/2023", formatter);
        LocalDateTime finishesAt1 = LocalDateTime.parse("20:30 30/03/2023", formatter);

        LocalDateTime startsAt2 = LocalDateTime.parse("14:30 07/04/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("15:30 07/04/2023", formatter);

        LocalDateTime startsAt3 = LocalDateTime.parse("16:30 15/04/2023", formatter);
        LocalDateTime finishesAt3 = LocalDateTime.parse("17:30 15/04/2023", formatter);


        a1 = new Appointment(p1, d1, r1, startsAt1, finishesAt1);
        a2 = new Appointment(p1, d1, r1, startsAt2, finishesAt2);
        a3 = new Appointment(p1, d1, r1, startsAt3, finishesAt3);
    }

    @Nested
    class PatientEntityTest {
        @Test
        void shouldCreatePatientInstanceTest() {
            entityManager.persistAndFlush(p1);
            Assertions.assertTrue(p1.getId() > 0);
        }

        @Test
        void shouldSetValuesInPatientTest() {
            entityManager.persistAndFlush(p1);

            Assertions.assertTrue(p1.getId() > 0);
            assertThat(p1.getFirstName()).isEqualTo("Juan");
            assertThat(p1.getLastName()).isEqualTo("Ruiz");
            assertThat(p1.getAge()).isEqualTo(34);
            assertThat(p1.getEmail()).isEqualTo("j.ruiz@hospital.accwe");
        }

        @Test
        void shouldRetrievePatientFromDatabaseTest() {
            Patient savedPatient = entityManager.persistAndFlush(p1);

            Patient retrievedPatient = entityManager.find(Patient.class, savedPatient.getId());
            assertThat(retrievedPatient).isEqualTo(savedPatient);
        }

        @Test
        void patientFieldsNotEmptyOrNullTest() {

            Assertions.assertNotNull(p1);
            Assertions.assertNotNull(p1.getFirstName());
            Assertions.assertNotNull(p1.getLastName());
            Assertions.assertTrue(p1.getAge() > 0);
            Assertions.assertNotNull(p1.getEmail());

            Assertions.assertNotEquals("", p1.getFirstName());
            Assertions.assertNotEquals("", p1.getLastName());
            Assertions.assertTrue(p1.getAge() > 0);
            Assertions.assertNotEquals("", p1.getEmail());
        }
    }
    @Nested
    class DoctorEntityTest {
        @Test
        void shouldCreateDoctorInstanceTest() {
            entityManager.persistAndFlush(d1);
            Assertions.assertTrue(d1.getId() > 0);
        }

        @Test
        void shouldSetValuesInDoctorTest() {
            entityManager.persistAndFlush(p1);

            Assertions.assertTrue(p1.getId() > 0);

            assertThat(d1.getFirstName()).isEqualTo("Julia");
            assertThat(d1.getLastName()).isEqualTo("Gomez");
            assertThat(d1.getAge()).isEqualTo(29);
            assertThat(d1.getEmail()).isEqualTo("j.gomez@hospital.accwe");
        }

        @Test
        void shouldRetrieveDoctorFromDatabaseTest() {
            Doctor savedDoctor = entityManager.persistAndFlush(d1);

            Doctor retrievedDoctor = entityManager.find(Doctor.class, savedDoctor.getId());
            assertThat(retrievedDoctor).isEqualTo(savedDoctor);
        }


        @Test
        void doctorFieldsNotEmptyOrNullTest() {

            Assertions.assertNotNull(d1);
            Assertions.assertNotNull(d1.getFirstName());
            Assertions.assertNotNull(d1.getLastName());
            Assertions.assertTrue(p1.getAge() > 0);
            Assertions.assertNotNull(d1.getEmail());

            Assertions.assertNotEquals("", d1.getFirstName());
            Assertions.assertNotEquals("", d1.getLastName());
            Assertions.assertTrue(p1.getAge() > 0);
            Assertions.assertNotEquals("", d1.getEmail());
        }

    }
@Nested
class RoomEntityTest {
    @Test
    void roomCreationTest() {
        entityManager.persistAndFlush(r1);

        Assertions.assertNotNull(r1.getRoomName());
        Room retrievedRoom = entityManager.find(Room.class, r1.getRoomName());
        Assertions.assertNotNull(retrievedRoom);

    }


    @Test
    void shouldCreateRoomInstanceTest() {
        String roomName = "Room101";
        r1 = new Room(roomName);
        Assertions.assertEquals(roomName, r1.getRoomName());
    }
}

    @Nested
    class AppointmentEntityTest {

        @Test
        void appointmentPersistenceTest() {
            entityManager.persistAndFlush(a1);
            entityManager.persistAndFlush(a2);
            entityManager.persistAndFlush(a3);

            Appointment A1 = entityManager.find(Appointment.class, a1.getId());
            Appointment A2 = entityManager.find(Appointment.class, a2.getId());
            Appointment A3 = entityManager.find(Appointment.class, a3.getId());

            Assertions.assertEquals(a1, A1);
            Assertions.assertEquals(a2, A2);
            Assertions.assertEquals(a3, A3);
        }
        @Test
        void appointmentCreationFieldsNotNullTest() {

            Assertions.assertNotNull(a1);
            Assertions.assertNotNull(a1.getPatient());
            Assertions.assertNotNull(a1.getDoctor());
            Assertions.assertNotNull(a1.getRoom());
            Assertions.assertNotNull(a1.getStartsAt());
            Assertions.assertNotNull(a1.getFinishesAt());

        }

        @Test
        void appointmentValidationTest() {
            Assertions.assertNotNull(a1.getStartsAt());
            Assertions.assertNotNull(a1.getFinishesAt());
            Assertions.assertTrue(a1.getStartsAt().isBefore(a1.getFinishesAt()));

        }
        @Test
        void overlapCasesTest() {
            // Case 1: A.starts == B.starts
            a1.setStartsAt(LocalDateTime.parse("19:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            a2.setStartsAt(LocalDateTime.parse("19:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            Assertions.assertTrue(a1.overlaps(a2));

            // Case 2: A.finishes == B.finishes
            a2.setFinishesAt(LocalDateTime.parse("20:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            a3.setFinishesAt(LocalDateTime.parse("20:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            Assertions.assertTrue(a2.overlaps(a3));

            // Case 3: A.starts < B.finishes && B.finishes < A.finishes
            a1.setStartsAt(LocalDateTime.parse("19:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            a2.setFinishesAt(LocalDateTime.parse("20:00 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            a3.setStartsAt(LocalDateTime.parse("19:45 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            Assertions.assertTrue(a1.overlaps(a2));
            Assertions.assertTrue(a2.overlaps(a3));

            // Case 4: B.starts < A.starts && A.finishes < B.finishes
            a1.setStartsAt(LocalDateTime.parse("19:45 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            a1.setFinishesAt(LocalDateTime.parse("20:15 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            a2.setStartsAt(LocalDateTime.parse("19:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            a3.setFinishesAt(LocalDateTime.parse("20:00 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            Assertions.assertTrue(a1.overlaps(a2));
            Assertions.assertTrue(a1.overlaps(a3));
        }

        }

    }












