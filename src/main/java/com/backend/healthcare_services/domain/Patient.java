package com.backend.healthcare_services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 300, message = "Size exceeded")
    @Column(length = 300)
    private String medicalHistory;

    @Size(max = 300, message = "Size exceeded")
    @Column(nullable = false, length = 300)
    private String disease;
}
