package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.*;
import com.backend.healthcare_services.domain.enumeration.AppointmentStatus;
import com.backend.healthcare_services.dto.AppointmentDTO;
import com.backend.healthcare_services.exception.BadRequestException;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import com.backend.healthcare_services.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final static String APPOINTMENT_NOT_FOUND_MSG = "appointment with id %d not found";

    public AppointmentDTO findById(Long id, Long patientId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Patient patient = patientRepository.findByIdAndUserIdOrderById(patientId, user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, patientId)));

        return appointmentRepository.findByIdAndPatientIdOrderById(id, patient).orElseThrow(() ->
                new ResourceNotFoundException(String.format(APPOINTMENT_NOT_FOUND_MSG, id)));
    }

    public List<AppointmentDTO> findByPatientId(Long patientId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Patient patient = patientRepository.findByIdAndUserIdOrderById(patientId, user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, patientId)));

        return appointmentRepository.findByPatientId(patient);
    }

    public Set<List<AppointmentDTO>> findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        List<Patient> patients = patientRepository.findByUserId(user);

        Set<List<AppointmentDTO>> appointments = new HashSet<>();

        for (Patient patient : patients) {
            if (appointmentRepository.findByPatientId(patient) != null)
                appointments.add(appointmentRepository.findByPatientId(patient));
        }

        return appointments;
    }

    public AppointmentDTO findById(Long id) {
        return appointmentRepository.findByIdOrderById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(APPOINTMENT_NOT_FOUND_MSG, id)));
    }

    public List<AppointmentDTO> findByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, patientId)));

        return appointmentRepository.findByPatientId(patient);
    }

    public List<AppointmentDTO> findAll() {
        return appointmentRepository.findAllBy();
    }

    public void addAppointment(Long userId, Doctor doctorId, Long patientId, AppointmentDTO appointmentDTO)
            throws BadRequestException {

        boolean checkStatus = reservationAvailability(doctorId, appointmentDTO.getAppointmentTime(),
                appointmentDTO.getAppointmentEndTime());

        if (!checkStatus)
            appointmentDTO.setStatus(AppointmentStatus.CREATED);
        else
            throw new BadRequestException("Appointment time is full! Please choose another");

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Doctor doctor = doctorRepository.findById(doctorId.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format(DOCTOR_NOT_FOUND_MSG, doctorId.getId())));

        Patient patient = patientRepository.findByIdAndUserIdOrderById(patientId, user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, patientId)));

        Department departments = departmentRepository.findByName(appointmentDTO.getDepartment())
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        Appointment appointment = new Appointment(doctor, patient, departments, appointmentDTO.getAppointmentTime(),
                appointmentDTO.getAppointmentEndTime(), AppointmentStatus.CREATED);

        appointmentRepository.save(appointment);
    }

    public void updateAppointment(Long id, Long userId, Doctor doctorId, Long patientId, AppointmentDTO appointmentDTO)
            throws BadRequestException {

        boolean checkStatus = reservationAvailability(doctorId, appointmentDTO.getAppointmentTime(),
                appointmentDTO.getAppointmentEndTime());

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Doctor doctor = doctorRepository.findById(doctorId.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format(DOCTOR_NOT_FOUND_MSG, doctorId.getId())));

        Patient patient = patientRepository.findByIdAndUserIdOrderById(patientId, user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, patientId)));

        Department departments = departmentRepository.findByName(appointmentDTO.getDepartment())
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        Appointment appointment = appointmentRepository.findByIdAndPatientId(id, patient).orElseThrow(() ->
                new ResourceNotFoundException(String.format(APPOINTMENT_NOT_FOUND_MSG, id)));

        if (appointmentDTO.getAppointmentTime().compareTo(appointment.getAppointmentTime()) == 0 &&
                appointmentDTO.getAppointmentEndTime().compareTo(appointment.getAppointmentEndTime()) == 0 &&
                doctorId.getId().equals(appointment.getDoctorId().getId()))
            appointment.setStatus(AppointmentStatus.UPDATED);
        else if (checkStatus)
            throw new BadRequestException("Appointment time is full! Please choose another");

        appointment.setDoctorId(doctor);
        appointment.setPatientId(patient);
        appointment.setDepartment(departments);
        appointment.setStatus(AppointmentStatus.UPDATED);
        appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
        appointment.setAppointmentEndTime(appointmentDTO.getAppointmentEndTime());

        appointmentRepository.save(appointment);
    }

    public void updateAppointmentAuth(Long id, Doctor doctorId, Long patientId, AppointmentDTO appointmentDTO)
            throws BadRequestException {

        boolean checkStatus = reservationAvailability(doctorId, appointmentDTO.getAppointmentTime(),
                appointmentDTO.getAppointmentEndTime());

        Doctor doctor = doctorRepository.findById(doctorId.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format(DOCTOR_NOT_FOUND_MSG, doctorId.getId())));

        Patient patient = patientRepository.findById(patientId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, patientId)));

        Department departments = departmentRepository.findByName(appointmentDTO.getDepartment())
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        Appointment appointment = appointmentRepository.findByIdAndPatientId(id, patient).orElseThrow(() ->
                new ResourceNotFoundException(String.format(APPOINTMENT_NOT_FOUND_MSG, id)));

        if (appointmentDTO.getAppointmentTime().compareTo(appointment.getAppointmentTime()) == 0 &&
                appointmentDTO.getAppointmentEndTime().compareTo(appointment.getAppointmentEndTime()) == 0 &&
                doctorId.getId().equals(appointment.getDoctorId().getId()))
            appointment.setStatus(AppointmentStatus.UPDATED);
        else if (checkStatus)
            throw new BadRequestException("Appointment time is full! Please choose another");

        appointment.setDoctorId(doctor);
        appointment.setPatientId(patient);
        appointment.setDepartment(departments);
        appointment.setStatus(AppointmentStatus.UPDATED);
        appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
        appointment.setAppointmentEndTime(appointmentDTO.getAppointmentEndTime());

        appointmentRepository.save(appointment);
    }

    public void removeById(Long id, Long userId, Long patientId) throws BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Patient patient = patientRepository.findByIdAndUserIdOrderById(patientId, user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, patientId)));

        appointmentRepository.findByIdAndPatientId(id, patient).orElseThrow(() ->
                new ResourceNotFoundException(String.format(APPOINTMENT_NOT_FOUND_MSG, id)));

        appointmentRepository.deleteById(id);
    }

    public void removeById(Long id) throws BadRequestException {
        boolean appointmentExists = appointmentRepository.existsById(id);

        if (!appointmentExists){
            throw new ResourceNotFoundException("appointment does not exist");
        }

        appointmentRepository.deleteById(id);
    }

    public boolean reservationAvailability(Doctor doctorId, LocalDateTime appointmentTime,
                                           LocalDateTime appointmentEndTime) {
        List<Appointment> checkStatus = appointmentRepository.checkStatus(doctorId, appointmentTime, appointmentEndTime,
                AppointmentStatus.DONE, AppointmentStatus.CANCELED);
        return checkStatus.size() > 0;
    }

    public Double price(Doctor doctorId) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId.getId());

        return doctor.get().getAppointmentFee();
    }
}
