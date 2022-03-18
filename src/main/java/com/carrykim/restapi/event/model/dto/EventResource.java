package com.carrykim.restapi.event.model.dto;

import com.carrykim.restapi.event.model.Event;
import org.springframework.hateoas.RepresentationModel;

public class EventResource extends RepresentationModel{

    private Event event;

    public EventResource(Event event){
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
