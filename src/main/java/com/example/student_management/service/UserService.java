package com.example.student_management.service;

import com.example.student_management.domain.User;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.UserRepository;
import com.example.student_management.security.authentication.CustomUserDetails;
import com.example.student_management.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Cacheable("user")
    public User findById(Long id) {
        if (id == null) {
            throw new DataInvalidException(String.format(ExceptionMessage.ID_INVALID.message, "User"));
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND_BY_ID.toString(), id));
        }
        return userOptional.get();
    }

    @Cacheable("user")
    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND_BY_EMAIL.toString(), email));
        }
        return userOptional.get();
    }

    @CachePut(value = "user")
    public User save(User user) {
        if (user.getRole() == null || user.getRole().getRoleName() == null) {
            throw new ForeignKeyException(String.format(ExceptionMessage.NULL_FOREIGN_KEY_REFERENCE.message, "Role"));
        }
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getRootCause() instanceof SQLException) {
                SQLException ex = (SQLException) e.getRootCause();
                String message = ServiceUtils.sqlExceptionMessageFormat(ex.getMessage());
                throw new ResourceConflictException(message);
            } else if (e.getRootCause() instanceof PropertyValueException) {
                PropertyValueException ex = (PropertyValueException) e.getRootCause();
                throw new DataInvalidException(ServiceUtils.propertyValueExceptionMessageFormat(ex.getMessage()));
            } else {
                throw new DataInvalidException(e.getMessage());
            }
        }
    }

    @Cacheable("user")
    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND_BY_USERNAME.toString(), username));
        }
        return userOptional.get();
    }

    @CacheEvict(value = "user")
    public void deleteById(Long id) {

        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND_BY_ID.toString(), id));
        }
    }


    @Override
    @Cacheable("user")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = this.findByUsername(username);
            return new CustomUserDetails(user);
        } catch (ResourceNotFoundException e) {
            throw new UsernameNotFoundException(username);
        }
    }
}
