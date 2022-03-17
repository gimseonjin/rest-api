package com.carrykim.restapi.event.infra;

import com.carrykim.restapi.event.model.Event;

public interface EventRepository {
    Event save(Event event);
}
