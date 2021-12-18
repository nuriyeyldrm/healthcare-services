package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.ConfirmationToken;
import com.backend.healthcare_services.domain.Role;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.domain.enumeration.UserRole;
import com.backend.healthcare_services.dto.AdminDTO;
import com.backend.healthcare_services.dto.UserDTO;
import com.backend.healthcare_services.exception.AuthException;
import com.backend.healthcare_services.exception.BadRequestException;
import com.backend.healthcare_services.exception.ConflictException;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import com.backend.healthcare_services.projection.ProjectUser;
import com.backend.healthcare_services.repository.ConfirmationTokenRepository;
import com.backend.healthcare_services.repository.RoleRepository;
import com.backend.healthcare_services.repository.UserRepository;
import com.backend.healthcare_services.service.email.EmailSender;
import com.backend.healthcare_services.service.email.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ConfirmationTokenService confirmationTokenService;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final EmailValidator emailValidator;

    private final EmailSender emailSender;

    private final PasswordEncoder passwordEncoder;

    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";

    public List<ProjectUser> fetchAllUsers(){
        return userRepository.findAllBy();
    }

    public ProjectUser findById(Long id) throws ResourceNotFoundException {
        return userRepository.findByIdOrderByFirstName(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
    }

    public void register(AdminDTO adminDTO) throws BadRequestException {

        boolean isValidEmail = emailValidator.test(adminDTO.getEmail());

        if (!isValidEmail){
            throw new IllegalStateException("email not valid");
        }

        if (userRepository.existsByEmail(adminDTO.getEmail())) {
            throw new ConflictException("Error: Email is already in use!");
        }

        if (adminDTO.getPassword() == null) {
            throw new BadRequestException("Please enter password");
        }

        String encodedPassword = passwordEncoder.encode(adminDTO.getPassword());

        Set<Role> roles = addRoles(adminDTO.getRoles());

        User user = new User(adminDTO.getFirstName(), adminDTO.getLastName(), encodedPassword,
                adminDTO.getPhoneNumber(), adminDTO.getEmail(), adminDTO.getAddress(), adminDTO.getZipCode(),
                roles, adminDTO.getAge(), adminDTO.getGender());

        userRepository.save(user);

        sendEmail(user);
    }

    @Transactional
    public String confirmToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).
                orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedDate() != null) {
            throw new IllegalStateException("e-mail already confirmed");
        }

        LocalDateTime expiredDate = confirmationToken.getExpiredDate();

        if (expiredDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedDate(token);
        userRepository.enableAppUser(confirmationToken.getUsers().getEmail());

        return "Email is confirmed! You can login right now :)";
    }

    public void login(String email, String password) throws AuthException {
        try {
            Optional<User> user = userRepository.findByEmail(email);

            if (!user.get().getEnabled()) {
                throw new IllegalStateException("e-mail not confirmed yet");
            }

            if (!BCrypt.checkpw(password, user.get().getPassword()))
                throw new AuthException("invalid credentials");
        } catch (Exception e) {
            throw new AuthException("invalid credentials");
        }
    }

    public String updateUser(Long id, UserDTO userDTO) throws BadRequestException {

        boolean emailExists = userRepository.existsByEmail(userDTO.getEmail());
        User userDetails = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));

        if (userDetails.getBuiltIn()){
            throw new ResourceNotFoundException("You dont have permission to update user info!");
        }

        if (emailExists && !userDTO.getEmail().equals(userDetails.getEmail())){
            throw new ConflictException("Error: Email is already in use!");
        }

        Set<String> userRoles = userDTO.getRoles();
        Set<Role> roles = addRoles(userRoles);

        if (userDTO.getEmail().equals(userDetails.getEmail())) {

            userDetails.setFirstName(userDTO.getFirstName());
            userDetails.setLastName(userDTO.getLastName());
            userDetails.setPhoneNumber(userDTO.getPhoneNumber());
            userDetails.setAddress(userDTO.getAddress());
            userDetails.setZipCode(userDTO.getZipCode());
            userDetails.setRoles(roles);
            userDetails.setAge(userDTO.getAge());
            userDetails.setGender(userDTO.getGender());

            userRepository.save(userDetails);
            return "Updated successfully!";
        }

        else {
            User user = new User(id, userDTO.getFirstName(), userDTO.getLastName(), userDetails.getPassword(),
                    userDTO.getPhoneNumber(), userDTO.getEmail(), userDTO.getAddress(), userDTO.getZipCode(), roles,
                    userDTO.getAge(), userDTO.getGender());

            userRepository.save(user);

            return sendEmail(user);
        }

    }

    public void updateUserAuth(Long id, AdminDTO adminDTO) throws BadRequestException {

        boolean emailExists = userRepository.existsByEmail(adminDTO.getEmail());
        User userDetails = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));

        if (userDetails.getBuiltIn()){
            throw new ResourceNotFoundException("You dont have permission to update user info!");
        }

        if (emailExists && !adminDTO.getEmail().equals(userDetails.getEmail())){
            throw new ConflictException("Error: Email is already in use!");
        }

        if (adminDTO.getPassword() == null) {
            adminDTO.setPassword(userDetails.getPassword());
        }

        else {
            String encodedPassword = passwordEncoder.encode(adminDTO.getPassword());
            adminDTO.setPassword(encodedPassword);
        }

        Set<String> userRoles = adminDTO.getRoles();
        Set<Role> roles = addRoles(userRoles);

        User user = new User(id, adminDTO.getFirstName(), adminDTO.getLastName(), adminDTO.getPassword(),
                adminDTO.getPhoneNumber(), adminDTO.getEmail(), adminDTO.getAddress(), adminDTO.getZipCode(),
                roles, adminDTO.getAge(), adminDTO.getGender());

        user.setEnabled(true);

        userRepository.save(user);
    }

    public void updatePassword(Long id, String newPassword, String oldPassword) throws BadRequestException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));

        if (user.getBuiltIn()){
            throw new ResourceNotFoundException("You dont have permission to update password!");
        }

        if (!(BCrypt.hashpw(oldPassword, user.getPassword()).equals(user.getPassword())))
            throw new BadRequestException("password does not match");

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public void removeById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));

        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByUsers(user);

        if (user.getBuiltIn()){
            throw new ResourceNotFoundException("You dont have permission to delete user!");
        }

        confirmationTokenRepository.deleteById(confirmationToken.get().getId());
        userRepository.deleteById(id);
    }

    public Set<Role> addRoles(Set<String> userRoles) {
        Set<Role> roles = new HashSet<>();

        if (userRoles == null) {
            Role patientRole = roleRepository.findByName(UserRole.ROLE_PATIENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(patientRole);
        } else {
            userRoles.forEach(role -> {
                switch (role) {
                    case "Administrator":
                        Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "Doctor":
                        Role doctorRole = roleRepository.findByName(UserRole.ROLE_DOCTOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(doctorRole);

                        break;
                    case "Nurse":
                        Role nurseRole = roleRepository.findByName(UserRole.ROLE_NURSE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(nurseRole);

                        break;
                    case "Secretary":
                        Role secretaryRole = roleRepository.findByName(UserRole.ROLE_SECRETARY)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(secretaryRole);

                        break;
                    default:
                        Role patientRole = roleRepository.findByName(UserRole.ROLE_PATIENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(patientRole);
                }
            });
        }

        return roles;
    }

    private String sendEmail(User user) {
        Optional<ConfirmationToken> confirmation = confirmationTokenRepository.findByUsers(user);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken;

        confirmationToken = confirmation.map(value ->
                new ConfirmationToken(value.getId(), token,
                        LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user))
                .orElseGet(() ->
                new ConfirmationToken(token, LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15), user));


        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = "http://localhost:8080/healthcare-services/api/user/confirm?token=" + token;

        emailSender.send(user.getEmail(), buildEmail(user.getFirstName(), link));
        return "Updated successfully! Please confirm your email!";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
