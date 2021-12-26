package com.backend.healthcare_services.controller;

import com.backend.healthcare_services.dto.AppointmentDTO;
import com.backend.healthcare_services.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments(){
        List<AppointmentDTO> appointments = appointmentService.findAll();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, Boolean>> addAppointment(HttpServletRequest request,
                                                          @RequestParam("doctorId") Long doctorId,
                                                          @RequestParam("patientId") Long patientId,
                                                          @Valid @RequestBody AppointmentDTO appointment) {
        Long userId = (Long) request.getAttribute("id");
        appointmentService.addAppointment(userId, doctorId, patientId, appointment);

        Map<String, Boolean> map = new HashMap<>();
        map.put("appointment appointment created successfully", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
}
