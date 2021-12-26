package com.backend.healthcare_services.dto;

import com.backend.healthcare_services.domain.Appointment;
import com.backend.healthcare_services.domain.enumeration.AppointmentStatus;
import com.backend.healthcare_services.domain.enumeration.Departments;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Long id;
    private DoctorOfAppointmentDTO doctorId;
    private PatientDTO patientId;
    private Departments department;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy HH:mm:ss", timezone = "Turkey")
    @NotNull(message = "Please enter the appointment time")
    private LocalDateTime appointmentTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy HH:mm:ss", timezone = "Turkey")
    @NotNull(message = "Please enter the appointment time")
    private LocalDateTime appointmentEndTime;
    private AppointmentStatus status;

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.doctorId = new DoctorOfAppointmentDTO(appointment.getDoctorId());
        this.patientId = new PatientDTO(appointment.getPatientId());
        this.department = appointment.getDepartment().getName();
        this.appointmentTime = appointment.getAppointmentTime();
        this.appointmentEndTime = appointment.getAppointmentEndTime();
        this.status = appointment.getStatus();
    }
}
