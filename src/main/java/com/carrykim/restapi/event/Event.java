package com.carrykim.restapi.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@EqualsAndHashCode(of = "id") @Builder
@NoArgsConstructor @AllArgsConstructor
public class Event {
    private Integer id;
    private String name;
    private String description;
    private EventStatus eventStatus = EventStatus.DRAFT;
}
