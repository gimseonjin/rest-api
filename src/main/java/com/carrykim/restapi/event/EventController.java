package com.carrykim.restapi.event;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController()
@RequestMapping(value = "/api/event", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    @PostMapping("")
    public ResponseEntity create(@RequestBody Event event) {

        URI uri = linkTo(methodOn(EventController.class).create(new Event())).slash("{id}").toUri();
        event.setId(10);
        return ResponseEntity.created(uri).body(event);
    }
}
