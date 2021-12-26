package com.backend.healthcare_services.dto;

import com.backend.healthcare_services.domain.Doctor;
import com.backend.healthcare_services.domain.FileDB;
import com.backend.healthcare_services.domain.enumeration.Departments;
import com.backend.healthcare_services.domain.enumeration.DoctorProfession;
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
public class DoctorDTO {

    private Long id;
    private UserDTO userId;
    private SecretaryOfDoctorDTO secretaryId;
    private DoctorProfession profession;
    private Departments department;
    private Double appointmentFee;
    private Boolean isAvailable;
    private Set<String> certificates;

    public DoctorDTO(Doctor doctor) {
        this.id = doctor.getId();
        this.userId = new UserDTO(doctor.getUserId());
        this.secretaryId = new SecretaryOfDoctorDTO(doctor.getDoctorsSecretary());
        this.profession = doctor.getProfession();
        this.department = doctor.getDepartment().getName();
        this.appointmentFee = doctor.getAppointmentFee();
        this.isAvailable = doctor.getIsAvailable();
        this.certificates = setFile(doctor.getCertificates());
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
