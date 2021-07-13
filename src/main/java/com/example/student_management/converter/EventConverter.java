package com.example.student_management.converter;

import com.example.student_management.domain.Event;
import com.example.student_management.dto.EventDto;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.message.ExceptionMessage;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {
    private final ClassConverter classConverter;

    public EventConverter(ClassConverter classConverter) {
        this.classConverter = classConverter;
    }

    public EventDto toDto(Event entity) {
        if(entity == null){
            return null;
        }
        return EventDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createDate(entity.getCreateDate())
                .status(entity.getStatus())
                .happenDate(entity.getHappenDate())
                .clazz(classConverter.toDto(entity.getClazz()))
                .build();
    }


    public Event toEntity(EventDto eventDto) {
        if (eventDto == null) {
            return null;
        }
        return Event.builder()
                .id(eventDto.getId())
                .name(eventDto.getName())
                .createDate(eventDto.getCreateDate())
                .status(eventDto.getStatus())
                .happenDate(eventDto.getHappenDate())
                .clazz(classConverter.toEntity(eventDto.getClazz()))
                .build();

    }
}
