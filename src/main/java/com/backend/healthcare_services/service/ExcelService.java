package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.helper.ExcelHelper;
import com.backend.healthcare_services.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@AllArgsConstructor
@Service
public class ExcelService {

    UserRepository userRepository;

    public ByteArrayInputStream loadUser() {
        List<User> users = userRepository.findAll();

        return ExcelHelper.usersExcel(users);
    }
}
