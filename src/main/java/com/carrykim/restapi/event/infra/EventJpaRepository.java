package com.carrykim.restapi.event.infra;

import com.carrykim.restapi.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventJpaRepository extends EventRepository ,JpaRepository<Event, Integer> {
}
