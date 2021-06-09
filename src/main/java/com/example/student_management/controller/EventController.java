package com.example.student_management.controller;

import com.example.student_management.converter.EventConverter;
import com.example.student_management.domain.Class;
import com.example.student_management.domain.Event;
import com.example.student_management.dto.EventDto;
import com.example.student_management.request.EventRequest;
import com.example.student_management.service.ClassService;
import com.example.student_management.service.EventService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private final EventConverter eventConverter;
    private final ClassService classService;

    public EventController(EventService eventService, EventConverter eventConverter, ClassService classService) {
        this.eventService = eventService;
        this.eventConverter = eventConverter;
        this.classService = classService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllEvents() {
        List<Event> events = eventService.findAll();
        List<EventDto> eventDtoList = events.stream().map(eventConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(eventDtoList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getEventById(@PathVariable("id") Long id) {
        Optional<Event> eventOptional = eventService.findById(id);
        if (eventOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        EventDto eventDto = eventConverter.toDto(eventOptional.get());

        return new ResponseEntity<>(eventDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addEvent(@RequestBody EventRequest request) {
        Optional<Class> classOptional = classService.findById(request.getClassId());
        if (classOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Event event = eventConverter.toEntity(request.getEvent());
        event.setClazz(classOptional.get());
        Event insertedEvent = eventService.save(event);
        return new ResponseEntity<>(eventConverter.toDto(insertedEvent), HttpStatus.CREATED);


    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateEvent(@PathVariable("id") Long id, @RequestBody EventRequest request) {
        Optional<Class> classOptional = classService.findById(request.getClassId());
        if (classOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Event> eventOptional = eventService.findById(id);
        if (eventOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Event eventUpdateInfo = eventConverter.toEntity(request.getEvent());
        eventUpdateInfo.setId(id);
        eventUpdateInfo.setClazz(classOptional.get());
        Event updatedEvent = eventService.save(eventUpdateInfo);
        return new ResponseEntity<>(eventConverter.toDto(updatedEvent), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteEvent(@PathVariable("id") Long id) {
        try {
            eventService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
