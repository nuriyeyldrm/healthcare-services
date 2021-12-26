package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.User;
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

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email) throws ConflictException;

    Optional<User> findByEmail(String email) throws ResourceNotFoundException;

    List<ProjectUser> findAllBy();

    Optional<ProjectUser> findByIdOrderByFirstName(Long id) throws ResourceNotFoundException;

    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE WHERE u.email =?1")
    void enableAppUser(String email);

//    @Transactional
//    @Modifying
//    @Query("UPDATE User u " +
//            "SET u.firstName = ?2, u.lastName = ?3, u.phoneNumber = ?4, u.address = ?5, u.zipCode = ?6, " +
//            "u.roles = ?7, u.age = ?8, u.gender = ?9 WHERE u.id = ?1")
//    void update(Long id, String firstName, String lastName, String phoneNumber, String address, String zipCode,
//                Set<Role> roles, Integer age, String gender) throws BadRequestException;

}
