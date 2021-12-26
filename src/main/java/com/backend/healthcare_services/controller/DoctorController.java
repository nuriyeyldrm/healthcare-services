package com.backend.healthcare_services.controller;

import com.backend.healthcare_services.dto.DoctorDTO;
import com.backend.healthcare_services.service.DoctorService;
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
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Boolean>> addDoctor(HttpServletRequest request,
                                                         @RequestParam("secretaryId") Long secretaryId,
                                                         @RequestParam("fileId") String fileId,
                                                         @Valid @RequestBody DoctorDTO doctor) {
        Long userId = (Long) request.getAttribute("id");
        doctorService.addDoctor(userId, secretaryId, fileId, doctor);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
}
