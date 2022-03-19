package com.carrykim.restapi.event;

import com.carrykim.restapi.event.infra.EventRepository;
import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.dto.EventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;


    private EventDto createEventDto(){
        return EventDto.builder()
                .name("My Event")
                .description("This is my first Event")
                .build();
    }

    private Event createEvent(int i){
        return Event.builder()
                .name("My Event : " + i)
                .description("This is my first Event")
                .build();
    }

    @Test
    public void create_event_success() throws Exception {
        //Given
        EventDto eventDto = createEventDto();

        //When
        //Then
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("event").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.update-event").exists());
    }

    @Test
    public void create_event_bad_request_empty_input() throws Exception {
        //Given
        EventDto eventDto = EventDto.builder().build();

        //When
        //Then
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("error").exists())
                .andExpect(jsonPath("message").exists());
    }

    @Test
    public void get_sorted_event_list_by_paging() throws Exception {
        // Given
        IntStream.range(1,30).forEach( i ->{
            Event event = createEvent(i);
            this.eventRepository.save(event);
        });

        //When
        //Then
        mockMvc.perform(get("/api/events")
                        .param("page","1")
                        .param("size","10")
                        .param("sort", "name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventResourceList.[0]._links.self").exists())
                .andExpect(jsonPath("_embedded.eventResourceList.[0]._links.profile").exists());
    }

    @Test
    public void get_event_success() throws Exception {
        //Given
        Event event = createEvent(0);
        this.eventRepository.save(event);

        //When
        //Then
        mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andDo(print())
                .andExpect(jsonPath("event").exists())
                .andExpect(jsonPath("_links").exists());
    }

    @Test
    public void get_event_not_found_event() throws Exception {
        //Given
        Integer wrongId = 10010;

        //When
        //Then
        mockMvc.perform(get("/api/events/{id}", wrongId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("error").exists())
                .andExpect(jsonPath("message").exists());
    }

    @Test
    public void update_event_success() throws Exception {
        //Given
        Event event = createEvent(11123);
        this.eventRepository.save(event);
        String newDescription = "new description";

        EventDto eventDto = EventDto.builder()
                .name(event.getName())
                .description(newDescription)
                .build();

        //When
        //Then
        mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(jsonPath("event.name").value(event.getName()))
                .andExpect(jsonPath("event.description").value(newDescription))
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("_links.profile").exists());
    }

    @Test
    public void update_event_bad_requset_empty_input() throws Exception {
        //Given
        Event event = createEvent(11223);
        this.eventRepository.save(event);

        EventDto eventDto = EventDto.builder()
                .build();

        //When
        //Then
        mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("error").exists())
                .andExpect(jsonPath("message").exists());
    }

    @Test
    public void update_event_not_found_wrong_id() throws Exception {
        //Given
        Integer wrongId = 10010;
        EventDto eventDto = createEventDto();

        //When
        //Then
        mockMvc.perform(put("/api/events/{id}", wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("error").exists())
                .andExpect(jsonPath("message").exists());
    }

}
