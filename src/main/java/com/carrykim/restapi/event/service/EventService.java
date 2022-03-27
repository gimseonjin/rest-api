package com.carrykim.restapi.event.service;

import com.carrykim.restapi.accounts.model.Account;
import com.carrykim.restapi.common.exception.CustomException;
import com.carrykim.restapi.event.infra.EventRepository;
import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.dto.EventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Event create(EventDto eventDto){
        Event newEvent = eventDto.toModel();
        newEvent.setManager(getPrincipal());
        return eventRepository.save(newEvent);
    }

    public Page<Event> readWithPage(Pageable pageable){
        return this.eventRepository.findAll(pageable);
    }

    public Event read(Integer id){
        Optional<Event> findEvent =  this.eventRepository.findById(id);
        validEventEmpty(findEvent);
        return findEvent.get();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Event update(Integer id, EventDto eventDto){
        Event event = this.read(id);
        validEventManager(event);
        event.update(eventDto);
        return this.eventRepository.save(event);
    }

    private Account getPrincipal(){
        return (Account)SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
    }

    private void validEventManager(Event event){
        Account currentAccount = getPrincipal();
        if(!event.getManager().equals(currentAccount))
            throw new CustomException(HttpStatus.UNAUTHORIZED, "이벤트 작성자가 아닙니다.");
    }

    private void validEventEmpty(Optional<Event> findEvent){
        if(findEvent.isEmpty())
            throw new CustomException(HttpStatus.NOT_FOUND, "Event not found");
    }

}
