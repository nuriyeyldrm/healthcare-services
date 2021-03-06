package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.Department;
import com.backend.healthcare_services.domain.Nurse;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.dto.NurseDTO;
import com.backend.healthcare_services.exception.BadRequestException;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import com.backend.healthcare_services.repository.DepartmentRepository;
import com.backend.healthcare_services.repository.NurseRepository;
import com.backend.healthcare_services.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class NurseService {

    private final NurseRepository nurseRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";
    private final static String NURSE_NOT_FOUND_MSG = "nurse with id %d not found";
    private final static String NURSE_WITH_USER_ID_NOT_FOUND_MSG = "nurse with user id %d not found";

    public NurseDTO findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        return nurseRepository.findByUserIdOrderByUserId(user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(NURSE_WITH_USER_ID_NOT_FOUND_MSG, userId)));
    }

    public NurseDTO findById(Long id) {
        return nurseRepository.findByIdOrderById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(NURSE_NOT_FOUND_MSG, id)));
    }

    public List<NurseDTO> findAll() {
        return nurseRepository.findAllBy();
    }

    public void addNurse(Long userId, NurseDTO nurseDTO) throws BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Department departments = departmentRepository.findByName(nurseDTO.getDepartment())
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        Nurse nurse = new Nurse(user, departments);

        nurseRepository.save(nurse);
    }

    public void updateNurse(Long userId, NurseDTO nurseDTO) throws BadRequestException{
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Nurse nurse = nurseRepository.findByUserId(user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(NURSE_WITH_USER_ID_NOT_FOUND_MSG, userId)));

        Department departments = departmentRepository.findByName(nurseDTO.getDepartment())
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        nurse.setDepartment(departments);

        nurseRepository.save(nurse);
    }

    public void deleteById(Long id) throws BadRequestException {
        nurseRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(NURSE_NOT_FOUND_MSG, id)));

        nurseRepository.deleteById(id);
    }
}