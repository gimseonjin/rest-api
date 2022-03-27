package com.carrykim.restapi.event.model;

import com.carrykim.restapi.accounts.model.Account;
import com.carrykim.restapi.common.serializer.AccountSerializer;
import com.carrykim.restapi.event.model.dto.EventDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;

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

    @ManyToOne
    @JsonSerialize(using = AccountSerializer.class)
    private Account manager;

    public void update(EventDto eventDto){
        this.setName(eventDto.getName());
        this.setDescription(eventDto.getDescription());
    }
}
