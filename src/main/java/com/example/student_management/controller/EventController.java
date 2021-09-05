package com.example.student_management.controller;

import com.example.student_management.converter.EventConverter;
import com.example.student_management.domain.Event;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.EventDto;
import com.example.student_management.service.EventService;
import com.example.student_management.specification.CustomSpecificationBuilder;
import com.example.student_management.utils.ServiceUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", ref = "unauthorized"),
        @ApiResponse(responseCode = "405", ref = "methodNotAllowed"),
        @ApiResponse(responseCode = "404", ref = "resourceNotFound"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
})
public class EventController {
    private final EventService eventService;
    private final EventConverter eventConverter;

    public EventController(EventService eventService, EventConverter eventConverter) {
        this.eventService = eventService;
        this.eventConverter = eventConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_events')")
    @Operation(summary = "Get list event")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> getAllEvents(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<Event> specification = new CustomSpecificationBuilder<Event>(ServiceUtils.getFilterParam(filter, Event.class)).build();
        List<Event> events = eventService.findAll(specification, pageable);
        List<EventDto> eventDtoList = events.stream().map(eventConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(eventDtoList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_view_event_by_id', 'can_view_all_events')")
    @Operation(summary = "Get event by id")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventDto.class)))
    public ResponseEntity<Object> getEventById(@PathVariable("id") Long id) {
        Event event = eventService.findById(id);
        EventDto eventDto = eventConverter.toDto(event);
        log.info("Get event {} successfully", id);
        return new ResponseEntity<>(eventDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_event')")
    @Operation(summary = "Create event")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventDto.class)))
    public ResponseEntity<Object> addEvent(@RequestBody EventDto eventDto) {
        Event event = eventConverter.toEntity(eventDto);
        event.setId(null);
        Event insertedEvent = eventService.save(event);
        log.info("Inserted event {}", insertedEvent.getId());
        return new ResponseEntity<>(eventConverter.toDto(insertedEvent), HttpStatus.CREATED);


    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_update_event')")
    @Operation(summary = "Update event")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventDto.class)))
    public ResponseEntity<Object> updateEvent(@PathVariable("id") Long id, @RequestBody EventDto eventDto) {
        Event updatedTarget = eventService.findById(id);
        Event updatedSource = eventConverter.toEntity(eventDto);
        updatedSource.setId(id);
        // Copy non-null properties from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        Event updatedEvent = eventService.save(updatedTarget);
        log.info("Updated event {}", id);
        return new ResponseEntity<>(eventConverter.toDto(updatedEvent), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_event_by_id')")
    @Operation(summary = "Delete event")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> deleteEvent(@PathVariable("id") Long id) {
        eventService.deleteById(id);
        log.info("Deleted event {}", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
