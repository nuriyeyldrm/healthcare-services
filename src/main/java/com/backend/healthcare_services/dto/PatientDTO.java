package com.backend.healthcare_services.dto;

import com.backend.healthcare_services.domain.FileDB;
import com.backend.healthcare_services.domain.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
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
    private Set<String> medicalImaging;
    private Set<String> prescriptions;
    private Set<String> bloodTests;
    private String diagnosis;

    public PatientDTO(Patient patient) {
        this.id = patient.getId();
        this.user = new UserDTO(patient.getUserId());
        this.medicalHistories = patient.getMedicalHistories();
        this.diseases = patient.getDiseases();
        this.medicalImaging = setFile(patient.getMedicalImaging());
        this.prescriptions = setFile(patient.getPrescriptions());
        this.bloodTests = setFile(patient.getBloodTests());
        this.diagnosis = patient.getDiagnosis();
    }

    public Set<String> setFile(Set<FileDB> files) {
        Set<String> out = new HashSet<>();
        FileDB[] file = files.toArray(new FileDB[files.size()]);

        for (int i = 0; i < files.size(); i++) {
            out.add(file[i].getId());

        }
        return out;
    }
}
