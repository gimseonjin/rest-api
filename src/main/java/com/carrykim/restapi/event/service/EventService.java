package com.carrykim.restapi.event.service;

import com.carrykim.restapi.event.global.exception.CustomException;
import com.carrykim.restapi.event.infra.EventRepository;
import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.dto.EventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event create(EventDto eventDto){
        Event newEvent = eventDto.toModel();
        return eventRepository.save(newEvent);
    }

    public Page<Event> readWithPage(Pageable pageable){
        return this.eventRepository.findAll(pageable);
    }

    public Event read(Integer id){
        Optional<Event> findEvent =  this.eventRepository.findById(id);
        if(findEvent.isEmpty())
            throw new CustomException(HttpStatus.NOT_FOUND, "Event not found");
        return findEvent.get();
    }

    public Event update(Integer id, EventDto eventDto){
        Event event = this.read(id);
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        return this.eventRepository.save(event);
    }

}
