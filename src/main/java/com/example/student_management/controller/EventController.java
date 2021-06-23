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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('can_view_all_events')")
    public ResponseEntity<Object> getAllEvents() {
        List<Event> events = eventService.findAll();
        List<EventDto> eventDtoList = events.stream().map(eventConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(eventDtoList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_view_event_by_id')")
    public ResponseEntity<Object> getEventById(@PathVariable("id") Long id) {
        Event event = eventService.findById(id);

        EventDto eventDto = eventConverter.toDto(event);

        return new ResponseEntity<>(eventDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_event')")
    public ResponseEntity<Object> addEvent(@RequestBody EventRequest request) {
        Class clazz = classService.findById(request.getClassId());

        Event event = eventConverter.toEntity(request.getEvent());
        event.setClazz(clazz);
        Event insertedEvent = eventService.save(event);
        return new ResponseEntity<>(eventConverter.toDto(insertedEvent), HttpStatus.CREATED);


    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_update_event')")
    public ResponseEntity<Object> updateEvent(@PathVariable("id") Long id, @RequestBody EventRequest request) {
        Class clazz = classService.findById(request.getClassId());

        Event event = eventService.findById(id);

        Event eventUpdateInfo = eventConverter.toEntity(request.getEvent());
        eventUpdateInfo.setId(event.getId());
        eventUpdateInfo.setClazz(clazz);
        Event updatedEvent = eventService.save(eventUpdateInfo);
        return new ResponseEntity<>(eventConverter.toDto(updatedEvent), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_event_by_id')")
    public ResponseEntity<Object> deleteEvent(@PathVariable("id") Long id) {
        eventService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
