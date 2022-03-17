package com.carrykim.restapi.event.model.dto;

import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.EventStatus;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class EventDto {
    private String name;
    private String description;

    public Event toModel(){
        return Event.builder()
                .name(this.name)
                .description(this.description)
                .eventStatus(EventStatus.DRAFT)
                .build();
    }
}
