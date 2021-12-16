package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.exception.BadRequestException;
import com.backend.healthcare_services.exception.ConflictException;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import com.backend.healthcare_services.projection.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email) throws ConflictException;

    Optional<User> findByEmail(String email) throws ResourceNotFoundException;

    List<ProjectUser> findAllBy();

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE WHERE u.email =?1")
    int enableAppUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.firstName = ?2, u.lastName = ?3, u.phoneNumber = ?4, u.email = ?5, u.address = ?6, " +
            "u.zipCode = ?7 WHERE u.id = ?1")
    void update(Long id, String firstName, String lastName, String phoneNumber, String email, String address,
                String zipCode) throws BadRequestException;
}
