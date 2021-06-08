package com.example.student_management.converter;

import com.example.student_management.domain.Event;
import com.example.student_management.dto.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {
    public EventDto toDto(Event entity){
        return EventDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createDate(entity.getCreateDate())
                .status(entity.getStatus())
                .happenDate(entity.getHappenDate())
                .clazz(entity.getClazz().getName())
                .build();
    }

}
