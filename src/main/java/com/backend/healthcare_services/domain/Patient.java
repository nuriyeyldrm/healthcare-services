package com.backend.healthcare_services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    @Size(max = 300, message = "Size exceeded")
    @Column(length = 300)
    private String medicalHistories;

    @Size(max = 300, message = "Size exceeded")
    @Column(nullable = false, length = 300)
    private String diseases;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "medical_imaging_id",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<FileDB> medicalImaging;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "patient_prescriptions",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<FileDB> prescriptions;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "patient_blood_tests",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<FileDB> bloodTests;

    @Size(max = 300, message = "Size exceeded")
    @Column(length = 300)
    private String diagnosis;
}
