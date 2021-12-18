package com.backend.healthcare_services.controller;

import com.backend.healthcare_services.domain.Patient;
import com.backend.healthcare_services.dto.PatientDTO;
import com.backend.healthcare_services.service.PatientService;
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
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/patient")
public class PatientController {

    public PatientService patientService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientDTO> getPatientById(HttpServletRequest request,
                                                     @PathVariable Long id){
        Long userId = (Long) request.getAttribute("id");
        PatientDTO patient = patientService.findById(id, userId);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, Boolean>> addPatient(HttpServletRequest request,
                                                           @Valid @RequestBody Patient patient) {
        Long userId = (Long) request.getAttribute("id");
        patientService.addPatient(userId, patient);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
}
