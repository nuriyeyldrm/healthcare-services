package com.backend.healthcare_services.dto;

import com.backend.healthcare_services.domain.Appointment;
import com.backend.healthcare_services.domain.enumeration.AppointmentStatus;
import com.backend.healthcare_services.domain.enumeration.Departments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Long id;
    private UserDTO userId;
    private DoctorDTO doctorId;
    private PatientDTO patientId;
    private Departments department;
    private LocalDateTime appointmentTime;
    private LocalDateTime appointmentEndTime;
    private AppointmentStatus status;

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.userId = new UserDTO(appointment.getUserId());
        this.doctorId = new DoctorDTO(appointment.getDoctorId());
        this.patientId = new PatientDTO(appointment.getPatientId());
        this.department = appointment.getDepartment().getName();
        this.appointmentTime = appointment.getAppointmentTime();
        this.appointmentEndTime = appointment.getAppointmentEndTime();
        this.status = appointment.getStatus();
    }
}
