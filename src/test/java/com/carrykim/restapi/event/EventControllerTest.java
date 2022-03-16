package com.carrykim.restapi.event;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EventControllerTest {

    private final MockMvc mockMvc;

    public EventControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /*
    * mockMVC를 통해서 이벤트를 만드는 기능을 테스트 합니다.
    * FIX >
    * hateoas에서 MediaType을 통해서 더 이상 HAL+JSON을 지원하지 않습니다.
    * 따라서 임의로 응답이 Application json으로 올 것이라고 수정했습니다.
    */
    @Test
    public void createEvent() throws Exception {
        mockMvc.perform(post("/api/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
