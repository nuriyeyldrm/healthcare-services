package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.Department;
import com.backend.healthcare_services.domain.Secretary;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.dto.SecretaryDTO;
import com.backend.healthcare_services.exception.BadRequestException;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import com.backend.healthcare_services.repository.DepartmentRepository;
import com.backend.healthcare_services.repository.SecretaryRepository;
import com.backend.healthcare_services.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SecretaryService {

    private final SecretaryRepository secretaryRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";
    private final static String SECRETARY_NOT_FOUND_MSG = "secretary with id %d not found";
    private final static String SECRETARY_WITH_USER_ID_NOT_FOUND_MSG = "secretary with user id %d not found";

    public SecretaryDTO findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        return secretaryRepository.findByUserIdOrderByUserId(user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(SECRETARY_WITH_USER_ID_NOT_FOUND_MSG, userId)));
    }

    public SecretaryDTO findById(Long id) {
        return secretaryRepository.findByIdOrderById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(SECRETARY_NOT_FOUND_MSG, id)));
    }

    public List<SecretaryDTO> findAll() {
        return secretaryRepository.findAllBy();
    }

    public void addSecretary(Long userId, SecretaryDTO secretaryDTO) throws BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Department departments = departmentRepository.findByName(secretaryDTO.getDepartment())
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        Secretary secretary = new Secretary(user, departments);

        secretaryRepository.save(secretary);
    }

    public void updateSecretary(Long userId, SecretaryDTO secretaryDTO) throws BadRequestException{
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Secretary secretary = secretaryRepository.findByUserId(user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(SECRETARY_WITH_USER_ID_NOT_FOUND_MSG, userId)));

        Department departments = departmentRepository.findByName(secretaryDTO.getDepartment())
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        secretary.setDepartment(departments);

        secretaryRepository.save(secretary);
    }

    public void deleteById(Long id) throws BadRequestException {
        secretaryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(SECRETARY_NOT_FOUND_MSG, id)));

        secretaryRepository.deleteById(id);
    }
}
