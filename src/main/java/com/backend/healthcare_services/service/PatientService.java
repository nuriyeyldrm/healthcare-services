package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.FileDB;
import com.backend.healthcare_services.domain.Patient;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.dto.PatientDTO;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import com.backend.healthcare_services.repository.FileDBRepository;
import com.backend.healthcare_services.repository.PatientRepository;
import com.backend.healthcare_services.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final FileDBRepository fileDBRepository;
    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";
    private final static String PATIENT_NOT_FOUND_MSG = "patient with id %d not found";

    public PatientDTO findById(Long id, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        return patientRepository.findByIdAndUserId(id, user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, id)));
    }

    public List<PatientDTO> findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        return patientRepository.findByUserId(user);
    }

    public PatientDTO findByIdAuth(Long id) {
        return patientRepository.findByIdOrderById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, id)));
    }

    public List<PatientDTO> findAll() {
        return patientRepository.findAllBy();
    }

    public void addPatient(Long userId, Patient patient) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        patient.setUserId(user);
        patientRepository.save(patient);
    }

    public void updatePatient(Long id, Long userId, Patient patient) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Patient patient1 = patientRepository.findByIdAndUserIdOrderById(id, user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, id)));

        patient1.setMedicalHistories(patient.getMedicalHistories());
        patient1.setDiseases(patient.getDiseases());

        patientRepository.save(patient1);
    }

    public void updatePatientAuth(Long id, String medicalImagingId, String prescriptionsId,
                                  String bloodTestsId, String diagnosis) {

        Patient patient = patientRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, id)));

        FileDB medicalImaging = fileDBRepository.findById(medicalImagingId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("medical image with id %s not found", id)));

        FileDB prescriptions = fileDBRepository.findById(prescriptionsId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("prescriptions with id %s not found", id)));

        FileDB bloodTests = fileDBRepository.findById(bloodTestsId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("blood tests with id %s not found", id)));


        patient.setMedicalImaging(setFile(medicalImaging));
        patient.setPrescriptions(setFile(prescriptions));
        patient.setBloodTests(setFile(bloodTests));
        patient.setDiagnosis(diagnosis);

        patientRepository.save(patient);
    }

    public Set<FileDB> setFile(FileDB file) {
        Set<FileDB> fileDBS = new HashSet<>();
        fileDBS.add(file);
        return fileDBS;
    }
}
