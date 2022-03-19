package com.carrykim.restapi.event.controller;

import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.dto.EventDto;
import com.carrykim.restapi.event.model.dto.EventResource;
import com.carrykim.restapi.event.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Api(tags = {"Event Controller"})
@RestController()
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @ApiOperation(value = "Event 객체를 추가하는 메소드")
    @PostMapping("")
    public ResponseEntity create(@RequestBody @Valid EventDto eventDto) {
        Event event = this.eventService.create(eventDto);
        EventResource eventResource = new EventResource(event);
        addLinks(eventResource, "/swagger-ui/index.html#/Event%20Controller/createUsingPOST");
        URI uri = linkTo(methodOn(EventController.class)
                .create(new EventDto()))
                .slash(eventResource.getEvent().getId()).toUri();
        return ResponseEntity.created(uri).body(eventResource);
    }

    @ApiOperation(value = "모든 Event 객체를 읽어오는 메소드")
    @GetMapping("")
    public ResponseEntity readAll(Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler) {
        var result =  pagedResourcesAssembler
                .toModel(this.eventService.readWithPage(pageable).map(event -> {
                    EventResource eventResource =  new EventResource(event);
                    addLinks(eventResource, "/swagger-ui/index.html#/Event%20Controller/readUsingGET");
                    return eventResource;
                }));
        result.add(new Link(getBaseURL() + "/swagger-ui/index.html#/Event%20Controller/readAllUsingGET","profile"));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Event 단일 객체를 읽어오는 메소드")
    @GetMapping("/{id}")
    public ResponseEntity read(@PathVariable Integer id){
        Event event = this.eventService.read(id);
        EventResource eventResource = new EventResource(event);
        addLinks(eventResource, "/swagger-ui/index.html#/Event%20Controller/readUsingGET");
        return ResponseEntity.ok(eventResource);
    }

    @ApiOperation(value = "이벤트 객체를 수정하는 메소드")
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody @Valid EventDto eventDto, @PathVariable Integer id){
        Event event = this.eventService.update(id, eventDto);
        EventResource eventResource = new EventResource(event);
        addLinks(eventResource, "/swagger-ui/index.html#/Event%20Controller/updateUsingPUT");
        return ResponseEntity.ok(eventResource);
    }

    private void addLinks(EventResource eventResource, String profileLink){
        WebMvcLinkBuilder selfAndUpdateLink =  linkTo(methodOn(EventController.class)
                .create(new EventDto()))
                .slash(eventResource.getEvent().getId());
        WebMvcLinkBuilder queryLink =  linkTo(methodOn(EventController.class));
        eventResource.add(queryLink.withRel("query-events"));
        eventResource.add(selfAndUpdateLink.withRel("update-event"));
        eventResource.add(selfAndUpdateLink.withSelfRel());
        eventResource.add(new Link(getBaseURL() + profileLink,"profile"));
    }

    private String getBaseURL(){
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }
}
