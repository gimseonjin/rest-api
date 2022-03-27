package com.carrykim.restapi.index.controller;

import com.carrykim.restapi.event.controller.EventController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController()
@RequestMapping(value = "/api", produces = MediaTypes.HAL_JSON_VALUE)
public class IndexController {
    @Value("${swagger.index}")
    private String swaggerIndexURL;

    @GetMapping("")
    public ResponseEntity index(){
        return ResponseEntity.ok()
                .body(new RepresentationModel()
                        .add(new Link(getBaseURL() + swaggerIndexURL, "profile"))
                        .add(linkTo(EventController.class).withRel("events")));
    }
    private String getBaseURL(){
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

}
