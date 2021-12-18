package com.backend.healthcare_services.dto;

import com.backend.healthcare_services.domain.FileDB;
import com.backend.healthcare_services.domain.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    private Long id;
    private Long userId;
    private String fullName;
    private String medicalHistories;
    private String diseases;
    private Set<FileDB> analyzes;

    public PatientDTO(Patient patient) {
        this.id = patient.getId();
        this.userId = patient.getUserId().getId();
        this.fullName = patient.getUserId().getFullName();
        this.medicalHistories = patient.getMedicalHistories();
        this.diseases = patient.getDiseases();
        this.analyzes = patient.getAnalyzes();
    }
}
