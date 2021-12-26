package com.backend.healthcare_services.dto;

import com.backend.healthcare_services.domain.Doctor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorOfAppointmentDTO {

    private String doctorFullName;
    private String contactPhoneNumber;
    private String contactEmail;
    private Double appointmentFee;

    public DoctorOfAppointmentDTO(Doctor doctor) {
        this.doctorFullName = doctor.getUserId().getFullName();
        this.contactPhoneNumber = doctor.getDoctorsSecretary().getUserId().getPhoneNumber();
        this.contactEmail = doctor.getDoctorsSecretary().getUserId().getEmail();
        this.appointmentFee = doctor.getAppointmentFee();
    }
}
