package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.UserDTO;
import com.cbielaszczuk.crm.dto.UserLoginDTO;
import com.cbielaszczuk.crm.dto.UserRegistrationDTO;
import com.cbielaszczuk.crm.mapper.UserMapper;
import com.cbielaszczuk.crm.model.UserModel;
import com.cbielaszczuk.crm.repository.UserRepository;
import com.cbielaszczuk.crm.validation.UserValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Registers a new user after validating the data.
     */
    public void register(UserRegistrationDTO registrationDTO) {
        UserValidator.validateRegistration(registrationDTO);
        UserDTO dto = new UserDTO(0L,
                registrationDTO.getName(),
                registrationDTO.getEmail(),
                registrationDTO.getPhone(),
                registrationDTO.getUsername(),
                registrationDTO.getPassword()
        );
        UserModel model = UserMapper.toModel(dto);
        repository.save(model);
    }

    /**
     * Authenticates a user by username and password.
     */
    @Transactional(readOnly = true)
    public UserDTO login(UserLoginDTO loginDTO) {
        UserValidator.validateLogin(loginDTO);
        UserModel model = repository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
        
        if (!model.getPassword().equals(loginDTO.getPassword())) {
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
        UserModel model = repository.findById(id)
                .orElse(null);
        return model != null ? UserMapper.toDTO(model) : null;
    }

    /**
     * Updates user data.
     */
    public void updateUser(UserDTO dto) {
        UserValidator.validateForCreateOrUpdate(dto);
        UserModel model = UserMapper.toModel(dto);
        repository.save(model);
    }

    /**
     * Soft-deletes a user.
     */
    public void deleteUser(Long id) {
        UserValidator.validateForDelete(id);
        repository.deleteById(id);
    }
}
