package com.example.student_management.service;

import com.example.student_management.domain.Event;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.EventRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(Long id) {
        if (id == null){
            throw new DataInvalidException(ExceptionMessage.ID_INVALID.message);
        }
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.EVENT_NOT_FOUND.toString(), id));
        }
        return eventOptional.get();
    }

    public Event save(Event event) {
        if (event.getName() == null || event.getName().isBlank()) {
            throw new DataInvalidException(ExceptionMessage.EVENT_NAME_INVALID.message);
        }
        if (event.getCreateDate() == null) {
            throw new DataInvalidException(ExceptionMessage.CREATE_DATE_INVALID.message);
        }
        if (event.getStatus() == null || event.getStatus().isBlank()) {
            throw new DataInvalidException(ExceptionMessage.EVENT_STATUS_INVALID.message);
        }
        return eventRepository.save(event);
    }

    public void deleteById(Long id) {
        try {
            eventRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.EVENT_NOT_FOUND.toString(), id));
        }
    }
}
