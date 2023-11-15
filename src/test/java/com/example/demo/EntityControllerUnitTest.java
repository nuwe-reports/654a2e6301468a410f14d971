
package com.example.demo;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;
import java.time.format.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * TODO
 * Implement all the unit test in its corresponding class.
 * Make sure to be as exhaustive as possible. Coverage is checked ;)
 */

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllDoctorsTest() throws Exception {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("Julia", "Gomez", 29, "j.gomez@email.com"));
        doctors.add(new Doctor("Alfredo", "Ferrero", 57, "a.ferrero@email.com"));
        doctors.add(new Doctor("Juan", "Ruiz", 44, "j.ruiz@email.com"));

        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName").value("Julia"))
                .andExpect(jsonPath("$[1].firstName").value("Alfredo"))
                .andExpect(jsonPath("$[2].firstName").value("Juan"))

                .andExpect(jsonPath("$[0].lastName").value("Gomez"))
                .andExpect(jsonPath("$[1].lastName").value("Ferrero"))
                .andExpect(jsonPath("$[2].lastName").value("Ruiz"))

                .andExpect(jsonPath("$[0].age").value(29))
                .andExpect(jsonPath("$[1].age").value(57))
                .andExpect(jsonPath("$[2].age").value(44))

                .andExpect(jsonPath("$[0].email").value("j.gomez@email.com"))
                .andExpect(jsonPath("$[1].email").value("a.ferrero@email.com"))
                .andExpect(jsonPath("$[2].email").value("j.ruiz@email.com"));
    }


    @Test
    void shouldGetNoDoctorsTest() throws Exception {
        List<Doctor> patients = new ArrayList<>();
        when(doctorRepository.findAll()).thenReturn(patients);
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isNoContent());

    }

    @Test
    void shouldGetDoctorByIdTest() throws Exception {

        Doctor doctor = new Doctor("Julia", "Gomez", 29, "j.gomez@email.com");

        doctor.setId(1);

        Optional<Doctor> optionalDoctor = Optional.of(doctor);

        when(doctorRepository.findById(doctor.getId())).thenReturn(optionalDoctor);

        mockMvc.perform(get("/api/doctors/{id}", doctor.getId()))
                .andExpect(status().isOk());

        assertThat(optionalDoctor).isPresent();
        assertThat(optionalDoctor.get().getId()).isEqualTo(doctor.getId());
    }

    @Test
    void shouldNotGetAnyDoctorByIdTest() throws Exception {
        long id = 2;
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/doctors/" + id))
                .andExpect(status().isNotFound());

    }


    @Test
    void shouldCreateDoctorTest() throws Exception {
        Doctor doctor = new Doctor("Julia", "Gomez", 29, "j.gomez@email.com");
        mockMvc.perform(post("/api/doctor")
                        .content(objectMapper.writeValueAsString(doctor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Julia"))
                .andExpect(jsonPath("$.lastName").value("Gomez"))
                .andExpect(jsonPath("$.age").value(29))
                .andExpect(jsonPath("$.email").value("j.gomez@email.com"));
    }


    @Test
    void shouldDeleteDoctorByIdTest() throws Exception {
        Doctor doctor = new Doctor("Julia", "Gomez", 29, "j.gomez@email.com");

        doctor.setId(15);

        Optional<Doctor> optionalDoctor = Optional.of(doctor);

        when(doctorRepository.findById(doctor.getId())).thenReturn(optionalDoctor);

        mockMvc.perform(delete("/api/doctors/{id}", doctor.getId()))
                .andExpect(status().isOk());

        assertThat(optionalDoctor).isPresent();
        assertThat(optionalDoctor.get().getId()).isEqualTo(doctor.getId());
    }

    @Test
    void shouldNotDeleteDoctorTest() throws Exception {
        long id = 4;
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/doctors/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldDeleteAlldoctorsTest() throws Exception {
        mockMvc.perform(delete("/api/doctors"))
                .andExpect(status().isOk());
    }

}

@WebMvcTest(PatientController.class)
class PatientControllerUnitTest {

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPatientsTest() throws Exception {
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com"));
        patients.add(new Patient("Paulino", "Antunez", 27, "p.antunez@email.com"));


        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Jose Luis"))
                .andExpect(jsonPath("$[1].firstName").value("Paulino"))

                .andExpect(jsonPath("$[0].lastName").value("Olaya"))
                .andExpect(jsonPath("$[1].lastName").value("Antunez"))

                .andExpect(jsonPath("$[0].age").value(37))
                .andExpect(jsonPath("$[1].age").value(27))

                .andExpect(jsonPath("$[0].email").value("j.olaya@email.com"))
                .andExpect(jsonPath("$[1].email").value("p.antunez@email.com"));
    }


    @Test
    void shouldGetNoPatients() throws Exception {
        List<Patient> patients = new ArrayList<>();
        when(patientRepository.findAll()).thenReturn(patients);
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isNoContent());

    }

    @Test
    void shouldGetPatientByIdTest() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");

        patient.setId(1);

        Optional<Patient> optionalPatient = Optional.of(patient);

        when(patientRepository.findById(patient.getId())).thenReturn(optionalPatient);

        mockMvc.perform(get("/api/patients/{id}", patient.getId()))
                .andExpect(status().isOk());

        assertThat(optionalPatient).isPresent();
        assertThat(optionalPatient.get().getId()).isEqualTo(patient.getId());
    }

    @Test
    void shouldNotGetAnyPatientByIdTest() throws Exception {
        long id = 11;
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patients/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldCreatePatientTest() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");

        mockMvc.perform(post("/api/patient")
                        .content(objectMapper.writeValueAsString(patient))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jose Luis"))
                .andExpect(jsonPath("$.lastName").value("Olaya"))
                .andExpect(jsonPath("$.age").value(37))
                .andExpect(jsonPath("$.email").value("j.olaya@email.com"));
    }


    @Test
    void shouldDeletePatientByIdTest() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");

        patient.setId(15);

        Optional<Patient> optionalPatient = Optional.of(patient);

        when(patientRepository.findById(patient.getId())).thenReturn(optionalPatient);

        mockMvc.perform(delete("/api/patients/{id}", patient.getId()))
                .andExpect(status().isOk());

        assertThat(optionalPatient).isPresent();
        assertThat(optionalPatient.get().getId()).isEqualTo(patient.getId());
    }

    @Test
    void shouldNotDeletePatientTest() throws Exception {
        long id = 11;
        when(patientRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/rooms/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldDeleteAllpatientsTest() throws Exception {
        mockMvc.perform(delete("/api/patients"))
                .andExpect(status().isOk());
    }

}


@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllRoomsTest() throws Exception {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room("Dermatology"));
        rooms.add(new Room("Operations"));
        rooms.add(new Room("Emergencies"));

        when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].roomName").value("Dermatology"))
                .andExpect(jsonPath("$[1].roomName").value("Operations"))
                .andExpect(jsonPath("$[2].roomName").value("Emergencies"));
    }

    @Test
    void shouldGetNoRoomsTest() throws Exception {
        List<Room> rooms = new ArrayList<>();
        when(roomRepository.findAll()).thenReturn(rooms);
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isNoContent());

    }

    @Test
    void shouldGetRoomByRoomNameTest() throws Exception {
        Room room = new Room("TestRoomName");
        when(roomRepository.findByRoomName("TestRoomName")).thenReturn(Optional.of(room));

        mockMvc.perform(get("/api/rooms/TestRoomName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomName").value("TestRoomName"));
    }

    @Test
    void shouldNotGetRoomTest() throws Exception {
        String roomName = "Ginecology";
        when(roomRepository.findByRoomName(roomName)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/rooms/" + roomName))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldCreateRoomTest() throws Exception {
        Room room = new Room("NewRoom");

        mockMvc.perform(post("/api/room")
                        .content(objectMapper.writeValueAsString(room))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomName").value("NewRoom"));
    }


    @Test
    void shouldDeleteRoomTest() throws Exception {
        when(roomRepository.findByRoomName("RoomToDelete")).thenReturn(Optional.of(new Room("RoomToDelete")));

        mockMvc.perform(delete("/api/rooms/RoomToDelete"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteRoomTest() throws Exception {
        String roomName = "Ginecology";
        when(roomRepository.findByRoomName(roomName)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/rooms/" + roomName))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldDeleteAllRoomsTest() throws Exception {
        mockMvc.perform(delete("/api/rooms"))
                .andExpect(status().isOk());
    }
}



