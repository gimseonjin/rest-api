package com.carrykim.restapi.event;

import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.dto.EventDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventTest {
    @Test
    public void build(){
        Event event = Event.builder().build();
        assertNotNull(event);
    }

    @Test
    public void javaBean(){
        //Given
        String eventName = "My Event";
        String eventDescription = "This is my event";

        //When
        Event event = Event.builder()
                .name(eventName)
                .description(eventDescription)
                .build();

        //Then
        assertEquals(eventName, event.getName());
        assertEquals(eventDescription, event.getDescription());
    }

    @Test
    public void fromDto(){
        //Given
        String eventName = "My Event";
        String eventDescription = "This is my event";

        Event event = Event.builder()
                .name(eventName)
                .description(eventDescription)
                .build();

        EventDto eventDto = EventDto.builder()
                .name(eventName)
                .description(eventDescription)
                .build();

        //When
        Event fromDto = eventDto.toModel();

        //Then
        assertEquals(event.getName(), fromDto.getName());
        assertEquals(event.getDescription(), fromDto.getDescription());
    }
}