package com.carrykim.restapi.event;

import com.carrykim.restapi.event.model.Event;
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
}