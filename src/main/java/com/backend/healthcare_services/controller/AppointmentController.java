package com.backend.healthcare_services.controller;

import com.backend.healthcare_services.domain.Doctor;
import com.backend.healthcare_services.dto.AppointmentDTO;
import com.backend.healthcare_services.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentDTO> getAppointmentById(HttpServletRequest request,
                                                     @RequestParam("appointmentId") Long appointmentId,
                                                     @RequestParam("patientId") Long patientId){
        Long userId = (Long) request.getAttribute("id");
        AppointmentDTO appointment = appointmentService.findById(appointmentId, patientId, userId);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatientId(HttpServletRequest request,
                                                                    @PathVariable Long patientId){
        Long userId = (Long) request.getAttribute("id");
        List<AppointmentDTO> appointment = appointmentService.findByPatientId(patientId, userId);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/all/appointmentsOfUser")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Set<List<AppointmentDTO>>> getAppointmentsByUserId(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("id");
        Set<List<AppointmentDTO>> appointment = appointmentService.findByUserId(userId);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<AppointmentDTO> getAppointmentByIdAuth(@PathVariable Long id){
        AppointmentDTO appointment = appointmentService.findById(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/{patientId}/patient")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatientIdAuth(@PathVariable Long patientId){
        List<AppointmentDTO> appointment = appointmentService.findByPatientId(patientId);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/all/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<Set<List<AppointmentDTO>>> getAppointmentsByUserIdAuth(@PathVariable Long userId){
        Set<List<AppointmentDTO>> appointment = appointmentService.findByUserId(userId);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments(){
        List<AppointmentDTO> appointments = appointmentService.findAll();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, Boolean>> addAppointment(HttpServletRequest request,
                                                          @RequestParam("doctorId") Doctor doctorId,
                                                          @RequestParam("patientId") Long patientId,
                                                          @Valid @RequestBody AppointmentDTO appointment) {
        Long userId = (Long) request.getAttribute("id");
        appointmentService.addAppointment(userId, doctorId, patientId, appointment);

        Map<String, Boolean> map = new HashMap<>();
        map.put("appointment appointment created successfully", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("/availability")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN') or hasRole('SECRETARY') " +
            "or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Object>> checkCarAvailability(
            @RequestParam (value = "doctorId") Doctor doctorId,
            @RequestParam (value = "appointmentTime")
            @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss")
                    LocalDateTime appointmentTime,
            @RequestParam (value = "appointmentEndTime")
            @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss")
                    LocalDateTime appointmentEndTime ){

        boolean availability = appointmentService.reservationAvailability(doctorId, appointmentTime, appointmentEndTime);
        Double appointmentFee = appointmentService.price(appointmentTime, appointmentEndTime, doctorId);

        Map<String, Object> map = new HashMap<>();
        map.put("isAvailable", !availability);
        map.put("appointmentFee", appointmentFee);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
