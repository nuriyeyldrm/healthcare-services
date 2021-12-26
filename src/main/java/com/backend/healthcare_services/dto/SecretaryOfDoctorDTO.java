package com.backend.healthcare_services.dto;

import com.backend.healthcare_services.domain.Secretary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SecretaryOfDoctorDTO {

    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;

    public SecretaryOfDoctorDTO(Secretary secretary) {
        this.id = secretary.getId();
        this.fullName = secretary.getUserId().getFullName();
        this.phoneNumber = secretary.getUserId().getPhoneNumber();
        this.email = secretary.getUserId().getEmail();
    }
}
