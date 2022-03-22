package com.carrykim.restapi.event.controller;

import com.carrykim.restapi.accounts.infra.UserRepository;
import com.carrykim.restapi.accounts.model.Account;
import com.carrykim.restapi.accounts.model.AccountRole;
import com.carrykim.restapi.accounts.service.AccountService;
import com.carrykim.restapi.event.infra.EventRepository;
import com.carrykim.restapi.event.model.Event;
import com.carrykim.restapi.event.model.dto.EventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;


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

    public Account createAccount(){
        return Account.builder()
                .name("kimseonjin616")
                .password("password")
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
    }

    public String getToken() throws Exception {
        String clientId = "myapp";
        String clientSecret = "pass";
        Account account = createAccount();
        accountService.create(account);

        var response =  mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(clientId, clientSecret))
                        .param("username", account.getUsername())
                        .param("password", "password")
                        .param("grant_type", "password")
                );
        var responseString = response.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser paser = new Jackson2JsonParser();
        return "Bearer " + paser.parseMap(responseString).get("access_token").toString();
    }

    @BeforeEach
    public void setUp(){
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void create_event_success() throws Exception {
        //Given
        EventDto eventDto = createEventDto();

        //When
        //Then
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getToken())
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
                        .header(HttpHeaders.AUTHORIZATION, getToken())
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("error").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("_links.index").exists());
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
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("_links.index").exists());
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
                        .header(HttpHeaders.AUTHORIZATION, getToken())
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
                        .header(HttpHeaders.AUTHORIZATION, getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("error").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("_links.index").exists());
    }

    @Test
    public void update_event_not_found_wrong_id() throws Exception {
        //Given
        Integer wrongId = 10010;
        EventDto eventDto = createEventDto();

        //When
        //Then
        mockMvc.perform(put("/api/events/{id}", wrongId)
                        .header(HttpHeaders.AUTHORIZATION, getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("error").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("_links.index").exists());
    }

}
