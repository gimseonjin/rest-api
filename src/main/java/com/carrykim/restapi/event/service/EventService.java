package com.carrykim.restapi.event.service;

import com.carrykim.restapi.event.infra.EventRepository;
import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.dto.EventDto;
import com.carrykim.restapi.event.model.dto.EventResource;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventResource create(EventDto eventDto){
        Event newEvent = eventDto.toModel();
        return new EventResource(eventRepository.save(newEvent));
    }

}
