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
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorDTO> getDoctorByUserId(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("id");
        DoctorDTO doctor = doctorService.findByUserId(userId);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id){
        DoctorDTO doctor = doctorService.findById(id);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<DoctorDTO> getDoctorByUserIdAuth(@PathVariable Long userId){
        DoctorDTO doctor = doctorService.findByUserId(userId);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(){
        List<DoctorDTO> doctors = doctorService.findAll();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

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

    @PostMapping("/add/auth")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Boolean>> addDoctorAuth(@RequestParam("userId") Long userId,
                                                          @RequestParam("secretaryId") Long secretaryId,
                                                          @RequestParam("fileId") String fileId,
                                                          @Valid @RequestBody DoctorDTO doctor) {
        doctorService.addDoctor(userId, secretaryId, fileId, doctor);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PutMapping("")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Boolean>> updateDoctor(HttpServletRequest request,
                                                             @RequestParam("secretaryId") Long secretaryId,
                                                             @RequestParam("fileId") String fileId,
                                                             @Valid @RequestBody DoctorDTO doctor) {
        Long userId = (Long) request.getAttribute("id");
        doctorService.updateDoctor(userId, secretaryId, fileId, doctor);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Boolean>> updateDoctorAuth(@RequestParam("userId") Long userId,
                                                                 @RequestParam("secretaryId") Long secretaryId,
                                                                 @RequestParam("fileId") String fileId,
                                                                 @Valid @RequestBody DoctorDTO doctor) {
        doctorService.updateDoctor(userId, secretaryId, fileId, doctor);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteById(id);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
