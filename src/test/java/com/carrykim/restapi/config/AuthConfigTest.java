package com.carrykim.restapi.config;

import com.carrykim.restapi.accounts.model.Account;
import com.carrykim.restapi.accounts.model.AccountRole;
import com.carrykim.restapi.accounts.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthConfigTest {

    @Autowired
    AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    public Account createAccount(){
        return Account.builder()
                .name("kimseonjin616")
                .password("password")
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
    }

    @Test
    public void getToken() throws Exception {
        String clientId = "myapp";
        String clientSecret = "pass";
        Account account = createAccount();
        accountService.create(account);

        mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(clientId, clientSecret))
                        .param("username", account.getUsername())
                        .param("password", "password")
                        .param("grant_type", "password")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }

}