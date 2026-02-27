package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.UserDTO;
import com.cbielaszczuk.crm.dto.UserLoginDTO;
import com.cbielaszczuk.crm.dto.UserRegistrationDTO;
import com.cbielaszczuk.crm.mapper.UserMapper;
import com.cbielaszczuk.crm.model.UserModel;
import com.cbielaszczuk.crm.repository.UserRepository;
import com.cbielaszczuk.crm.validation.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user after validating the data. Password is hashed with BCrypt.
     */
    public void register(UserRegistrationDTO registrationDTO) {
        UserValidator.validateRegistration(registrationDTO);

        if (repository.existsByUsername(registrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username '" + registrationDTO.getUsername() + "' is already taken.");
        }
        if (repository.existsByEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email '" + registrationDTO.getEmail() + "' is already registered.");
        }

        UserModel model = new UserModel(
                null,
                registrationDTO.getName(),
                registrationDTO.getEmail(),
                registrationDTO.getPhone(),
                registrationDTO.getUsername(),
                passwordEncoder.encode(registrationDTO.getPassword())
        );
        repository.save(model);
    }

    /**
     * Authenticates a user by username and password using BCrypt verification.
     */
    @Transactional(readOnly = true)
    public UserDTO login(UserLoginDTO loginDTO) {
        UserValidator.validateLogin(loginDTO);
        UserModel model = repository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        if (!passwordEncoder.matches(loginDTO.getPassword(), model.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return UserMapper.toDTO(model);
    }

    /**
     * Returns all users as DTOs.
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns a single user by ID.
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        return repository.findById(id)
                .map(UserMapper::toDTO)
                .orElse(null);
    }

    /**
     * Updates user data. If password changed, re-hashes it.
     */
    public void updateUser(UserDTO dto) {
        UserValidator.validateForCreateOrUpdate(dto);
        UserModel existing = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setUsername(dto.getUsername());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()
                && !dto.getPassword().startsWith("$2a$")) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        repository.save(existing);
    }

    /**
     * Deletes a user by ID.
     */
    public void deleteUser(Long id) {
        UserValidator.validateForDelete(id);
        repository.deleteById(id);
    }
}
