package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Appointment;
import com.backend.healthcare_services.dto.AppointmentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<AppointmentDTO> findAllBy();
}
