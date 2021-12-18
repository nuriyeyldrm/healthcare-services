package com.backend.healthcare_services.domain;

import com.backend.healthcare_services.domain.enumeration.DoctorProfession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "secretary_id", referencedColumnName = "id")
    private Secretary doctorsSecretary;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private DoctorProfession profession;

    @Column(nullable = false)
    private Double appointmentFee;

    @Column(nullable = false)
    private Boolean isAvailable;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "doctor_certificates",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<FileDB> certificates;
}
