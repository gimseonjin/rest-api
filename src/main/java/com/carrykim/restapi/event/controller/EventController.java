package com.carrykim.restapi.event.controller;

import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.dto.EventDto;
import com.carrykim.restapi.event.model.dto.EventResource;
import com.carrykim.restapi.event.service.EventService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody @Valid EventDto eventDto) {
        EventResource eventResource = this.eventService.create(eventDto);
        WebMvcLinkBuilder selfAndUpdateLink =  linkTo(methodOn(EventController.class)
                .create(new EventDto()))
                .slash(eventResource.getEvent().getId());
        WebMvcLinkBuilder queryLink =  linkTo(methodOn(EventController.class));
        eventResource.add(queryLink.withRel("query-events"));
        eventResource.add(selfAndUpdateLink.withRel("update-event"));
        eventResource.add(selfAndUpdateLink.withSelfRel());
        URI uri = selfAndUpdateLink.toUri();
        return ResponseEntity.created(uri).body(eventResource);
    }
}
