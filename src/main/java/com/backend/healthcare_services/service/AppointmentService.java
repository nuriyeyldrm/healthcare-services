package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.*;
import com.backend.healthcare_services.domain.enumeration.AppointmentStatus;
import com.backend.healthcare_services.dto.AppointmentDTO;
import com.backend.healthcare_services.exception.BadRequestException;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import com.backend.healthcare_services.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;
    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";
    private final static String DOCTOR_NOT_FOUND_MSG = "doctor with id %d not found";
    private final static String PATIENT_NOT_FOUND_MSG = "patient with id %d not found";

    public List<AppointmentDTO> findAll() {
        return appointmentRepository.findAllBy();
    }

    public void addAppointment(Long userId, Long doctorId, Long patientId, AppointmentDTO appointmentDTO)
            throws BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(DOCTOR_NOT_FOUND_MSG, doctorId)));

        Patient patient = patientRepository.findByIdAndUserIdOrderById(patientId, user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, patientId)));

        Department departments = departmentRepository.findByName(appointmentDTO.getDepartment())
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        Appointment appointment = new Appointment(doctor, patient, departments, appointmentDTO.getAppointmentTime(),
                appointmentDTO.getAppointmentEndTime(), AppointmentStatus.CREATED);

        appointmentRepository.save(appointment);
    }
}
