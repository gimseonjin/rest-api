package com.carrykim.restapi.event.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter @Setter
@EqualsAndHashCode(of = "id") @Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Event {
    @Id @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;
    @Column()
    private String description;
}
