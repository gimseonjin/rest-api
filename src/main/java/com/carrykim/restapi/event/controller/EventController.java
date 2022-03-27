package com.carrykim.restapi.event.controller;

import com.carrykim.restapi.accounts.model.Account;
import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.dto.EventDto;
import com.carrykim.restapi.event.model.dto.EventResource;
import com.carrykim.restapi.event.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private EventService eventService;

    @Value("${swagger.event.create}")
    private String swaggerCreateEventURL;

    @Value("${swagger.event.update}")
    private String swaggerUpdateEventURL;

    @Value("${swagger.event.readAll}")
    private String swaggerReadAllEventURL;

    @Value("${swagger.event.read}")
    private String swaggerReadEventURL;

    @ApiOperation(value = "Event 객체를 추가하는 메소드")
    @PostMapping("")
    public ResponseEntity create(@RequestBody @Valid EventDto eventDto) {

        Event event = this.eventService.create(eventDto);
        EventResource eventResource = createEventResource(event, swaggerCreateEventURL);
        URI uri = getSelfAndUpdateLink(eventResource).toUri();
        return ResponseEntity.created(uri).body(eventResource);
    }

    @ApiOperation(value = "모든 Event 객체를 읽어오는 메소드")
    @GetMapping("")
    public ResponseEntity readAll(Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler) {
        var result =  createPagingModel(pageable, pagedResourcesAssembler);
        addProfileLink(result, swaggerUpdateEventURL);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Event 단일 객체를 읽어오는 메소드")
    @GetMapping("/{id}")
    public ResponseEntity read(@PathVariable Integer id){
        Event event = this.eventService.read(id);
        EventResource eventResource = createEventResource(event, swaggerReadAllEventURL);
        return ResponseEntity.ok(eventResource);
    }

    @ApiOperation(value = "이벤트 객체를 수정하는 메소드")
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody @Valid EventDto eventDto, @PathVariable Integer id){
        Event event = this.eventService.update(id, eventDto);
        EventResource eventResource = createEventResource(event, swaggerReadEventURL);
        return ResponseEntity.ok(eventResource);
    }

    private EventResource createEventResource(Event event, String profileLink){
        EventResource eventResource = new EventResource(event);
        addLinks(eventResource, profileLink);
        return eventResource;
    }

    private PagedModel createPagingModel(Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler){
        return pagedResourcesAssembler
                .toModel(this.eventService.readWithPage(pageable).map(event -> {
                    EventResource eventResource = createEventResource(event, swaggerReadEventURL);
                    return eventResource;
                }));
    }

    private void addLinks(EventResource eventResource, String profileLink){
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal.getClass().equals(Account.class)){
            addCreateLink(eventResource);
            addUpdateLink(eventResource);
        }

        addQueryLink(eventResource);
        addSelfLink(eventResource);
        addProfileLink(eventResource, profileLink);
    }

    private void addUpdateLink(EventResource eventResource){
        WebMvcLinkBuilder selfAndUpdateLink = getSelfAndUpdateLink(eventResource);
        eventResource.add(selfAndUpdateLink.withRel("update-event"));
    }

    private void addSelfLink(EventResource eventResource){
        WebMvcLinkBuilder selfAndUpdateLink =  getSelfAndUpdateLink(eventResource);
        eventResource.add(selfAndUpdateLink.withSelfRel());
    }

    private void addQueryLink(EventResource eventResource){
        WebMvcLinkBuilder queryLink =  linkTo(EventController.class);
        eventResource.add(queryLink.withRel("query-events"));
    }

    private void addCreateLink(RepresentationModel eventResource){
        WebMvcLinkBuilder queryLink =  linkTo(methodOn(EventController.class).create(new EventDto()));
        eventResource.add(queryLink.withRel("create-events"));
    }

    private void addProfileLink(RepresentationModel eventResource, String profileLink){
        eventResource.add(new Link(getBaseURL() + profileLink,"profile"));
    }

    private WebMvcLinkBuilder getSelfAndUpdateLink(EventResource eventResource){
        return linkTo(methodOn(EventController.class)
                .create(new EventDto()))
                .slash(eventResource.getEvent().getId());
    }

    private String getBaseURL(){
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }
}
