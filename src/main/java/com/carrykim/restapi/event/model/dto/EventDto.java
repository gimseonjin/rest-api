package com.carrykim.restapi.event.model.dto;

import com.carrykim.restapi.event.model.Event;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class EventDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;

    public Event toModel(){
        return Event.builder()
                .name(this.name)
                .description(this.description)
                .build();
    }
}
