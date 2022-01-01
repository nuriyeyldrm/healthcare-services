package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Appointment;
import com.backend.healthcare_services.domain.Doctor;
import com.backend.healthcare_services.domain.Patient;
import com.backend.healthcare_services.domain.enumeration.AppointmentStatus;
import com.backend.healthcare_services.dto.AppointmentDTO;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<AppointmentDTO> findByIdAndPatientIdOrderById(Long id, Patient patientId) throws ResourceNotFoundException;

    Optional<AppointmentDTO> findByIdOrderById(Long id) throws ResourceNotFoundException;

    Optional<Appointment> findByIdAndPatientId(Long id, Patient patient) throws ResourceNotFoundException;

    List<AppointmentDTO> findByPatientId(Patient patientId);

    List<AppointmentDTO> findAllBy();

    @Query("SELECT a FROM Appointment a " +
            "WHERE (a.doctorId = ?1 and a.status <> ?4 and a.status <> ?5 and " +
            "?2 BETWEEN a.appointmentTime and a.appointmentEndTime) or " +
            "(a.doctorId = ?1 and a.status <> ?4 and a.status <> ?5 and " +
            "?3 BETWEEN a.appointmentTime and a.appointmentEndTime)")
    List<Appointment> checkStatus(Doctor doctorId, LocalDateTime appointmentTime, LocalDateTime appointmentEndTime,
                                  AppointmentStatus done, AppointmentStatus canceled);
}
