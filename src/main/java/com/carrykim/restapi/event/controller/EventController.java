package com.carrykim.restapi.event.controller;

import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.infra.EventRepository;
import com.carrykim.restapi.event.model.dto.EventDto;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController()
@RequestMapping(value = "/api/event", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody @Valid EventDto eventDto) {
        Event newEvent = this.eventRepository.save(eventDto.toModel());
        URI uri = linkTo(methodOn(EventController.class).create(new EventDto())).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(uri).body(newEvent);
    }
}
