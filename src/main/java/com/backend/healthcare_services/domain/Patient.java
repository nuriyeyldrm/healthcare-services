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

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    @Size(max = 300, message = "Size exceeded")
    @Column(length = 300)
    private String medicalHistories;

    @Size(max = 300, message = "Size exceeded")
    @Column(nullable = false, length = 300)
    private String diseases;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "patient_documents",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<FileDB> analyzes;
}
