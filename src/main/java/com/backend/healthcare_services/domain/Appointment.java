package com.backend.healthcare_services.domain;

import com.backend.healthcare_services.domain.enumeration.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctorId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patientId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy HH:mm:ss", timezone = "Turkey")
    @NotNull(message = "Please enter the appointment time")
    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy HH:mm:ss", timezone = "Turkey")
    @NotNull(message = "Please enter the appointment time")
    @Column(nullable = false)
    private LocalDateTime appointmentEndTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private AppointmentStatus status;

    public Appointment(Doctor doctorId, Patient patientId, Department department, LocalDateTime appointmentTime,
                       LocalDateTime appointmentEndTime, AppointmentStatus status) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.department = department;
        this.appointmentTime = appointmentTime;
        this.appointmentEndTime = appointmentEndTime;
        this.status = status;
    }
}
