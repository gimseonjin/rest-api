package com.carrykim.restapi.event.service;

import com.carrykim.restapi.event.infra.EventJpaRepository;
import com.carrykim.restapi.event.infra.EventRepository;
import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.dto.EventDto;
import com.carrykim.restapi.event.model.dto.EventResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

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

}
