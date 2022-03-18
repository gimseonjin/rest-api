package com.carrykim.restapi.event.controller;

import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.dto.EventDto;
import com.carrykim.restapi.event.model.dto.EventResource;
import com.carrykim.restapi.event.service.EventService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Event event = this.eventService.create(eventDto);
        EventResource eventResource = new EventResource(event);
        addLinks(eventResource);
        URI uri = linkTo(methodOn(EventController.class)
                .create(new EventDto()))
                .slash(eventResource.getEvent().getId()).toUri();
        return ResponseEntity.created(uri).body(eventResource);
    }

    @GetMapping("")
    public ResponseEntity readAll(Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler) {
        var result =  pagedResourcesAssembler
                .toModel(this.eventService.readWithPage(pageable).map(event -> {
                    EventResource eventResource =  new EventResource(event);
                    addLinks(eventResource);
                    return eventResource;
                }));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity read(@PathVariable Integer id){
        Event event = this.eventService.read(id);
        EventResource eventResource = new EventResource(event);
        addLinks(eventResource);
        return ResponseEntity.ok(eventResource);
    }

    private void addLinks(EventResource eventResource){
        WebMvcLinkBuilder selfAndUpdateLink =  linkTo(methodOn(EventController.class)
                .create(new EventDto()))
                .slash(eventResource.getEvent().getId());
        WebMvcLinkBuilder queryLink =  linkTo(methodOn(EventController.class));
        eventResource.add(queryLink.withRel("query-events"));
        eventResource.add(selfAndUpdateLink.withRel("update-event"));
        eventResource.add(selfAndUpdateLink.withSelfRel());
    }
}
