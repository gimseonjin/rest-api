package com.carrykim.restapi.event.infra;

import com.carrykim.restapi.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EventRepository {
    Event save(Event event);
    Page<Event> findAll(Pageable pageable);
}
