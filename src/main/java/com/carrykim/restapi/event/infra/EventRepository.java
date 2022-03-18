package com.carrykim.restapi.event.infra;

import com.carrykim.restapi.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    Page<Event> findAll(Pageable pageable);
    Optional<Event> findById(Integer id);
}
