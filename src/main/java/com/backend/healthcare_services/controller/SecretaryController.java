package com.backend.healthcare_services.controller;

import com.backend.healthcare_services.dto.SecretaryDTO;
import com.backend.healthcare_services.service.SecretaryService;
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
@RequestMapping("/secretary")
public class SecretaryController {

    private final SecretaryService secretaryService;

    @GetMapping("")
    @PreAuthorize("hasRole('SECRETARY')")
    public ResponseEntity<SecretaryDTO> getSecretaryByUserId(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("id");
        SecretaryDTO secretary = secretaryService.findByUserId(userId);
        return new ResponseEntity<>(secretary, HttpStatus.OK);
    }

    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<SecretaryDTO> getSecretaryById(@PathVariable Long id){
        SecretaryDTO secretary = secretaryService.findById(id);
        return new ResponseEntity<>(secretary, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<SecretaryDTO> getSecretaryByUserIdAuth(@PathVariable Long userId){
        SecretaryDTO secretary = secretaryService.findByUserId(userId);
        return new ResponseEntity<>(secretary, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<List<SecretaryDTO>> getAllSecretaries(){
        List<SecretaryDTO> secretaries = secretaryService.findAll();
        return new ResponseEntity<>(secretaries, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('SECRETARY')")
    public ResponseEntity<Map<String, Boolean>> addSecretary(HttpServletRequest request,
                                                         @Valid @RequestBody SecretaryDTO secretary) {
        Long userId = (Long) request.getAttribute("id");
        secretaryService.addSecretary(userId, secretary);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Boolean>> addSecretaryAuth(@PathVariable Long userId,
                                                             @Valid @RequestBody SecretaryDTO secretary) {
        secretaryService.addSecretary(userId, secretary);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PutMapping("")
    @PreAuthorize("hasRole('SECRETARY')")
    public ResponseEntity<Map<String, Boolean>> updateSecretary(HttpServletRequest request,
                                                            @Valid @RequestBody SecretaryDTO secretary) {
        Long userId = (Long) request.getAttribute("id");
        secretaryService.updateSecretary(userId, secretary);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY') or hasRole('NURSE') or hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Boolean>> updateSecretaryAuth(@PathVariable Long userId,
                                                                @Valid @RequestBody SecretaryDTO secretary) {
        secretaryService.updateSecretary(userId, secretary);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteSecretary(@PathVariable Long id) {
        secretaryService.deleteById(id);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
