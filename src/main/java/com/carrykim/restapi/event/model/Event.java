package com.carrykim.restapi.event.model;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@EqualsAndHashCode(of = "id") @Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Event {
    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;
}
