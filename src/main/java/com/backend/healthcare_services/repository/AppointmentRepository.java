package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Appointment;
import com.backend.healthcare_services.domain.Patient;
import com.backend.healthcare_services.dto.AppointmentDTO;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<AppointmentDTO> findByIdAndPatientId(Long id, Patient patientId) throws ResourceNotFoundException;

    Optional<AppointmentDTO> findByIdOrderById(Long id) throws ResourceNotFoundException;

    List<AppointmentDTO> findByPatientId(Patient patientId);

    List<AppointmentDTO> findAllBy();
}
