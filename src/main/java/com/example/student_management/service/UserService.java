package com.example.student_management.service;

import com.example.student_management.domain.User;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.UserRepository;
import com.example.student_management.security.authentication.CustomUserDetails;
import com.example.student_management.utils.ServiceUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND_BY_ID.toString(), id));
        }
        return userOptional.get();
    }

    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND_BY_EMAIL.toString(), email));
        }
        return userOptional.get();
    }

    public User save(User user) {
        // TODO: handle user add id
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            SQLException ex = (SQLException) e.getRootCause();
            String message = ServiceUtils.messageFormat(ex.getMessage());
            throw new ResourceConflictException(message);
        }
    }

    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND_BY_USERNAME.toString(), username));
        }
        return userOptional.get();
    }

    public void deleteById(Long id) {

        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND_BY_ID.toString(), id));
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = this.findByUsername(username);
            return new CustomUserDetails(user);
        } catch (ResourceNotFoundException e) {
            throw new UsernameNotFoundException(username);
        }
    }
}
