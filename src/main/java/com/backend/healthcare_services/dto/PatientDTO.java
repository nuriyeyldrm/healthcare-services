package com.backend.healthcare_services.dto;

import com.backend.healthcare_services.domain.FileDB;
import com.backend.healthcare_services.domain.Patient;
import com.backend.healthcare_services.projection.ProjectUser;
import com.backend.healthcare_services.repository.UserRepository;
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
    private UserDTO user;
    private String medicalHistories;
    private String diseases;
    private Set<FileDB> medicalImaging;
    private Set<FileDB> prescriptions;
    private Set<FileDB> bloodTests;

    public PatientDTO(Patient patient) {
        this.id = patient.getId();
        this.user = new UserDTO(patient.getUserId());
        this.medicalHistories = patient.getMedicalHistories();
        this.diseases = patient.getDiseases();
        this.medicalImaging = patient.getMedicalImaging();
        this.prescriptions = patient.getPrescriptions();
        this.bloodTests = patient.getBloodTests();
    }
}
