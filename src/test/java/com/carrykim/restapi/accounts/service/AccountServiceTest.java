package com.carrykim.restapi.accounts.service;

import com.carrykim.restapi.accounts.infra.UserRepository;
import com.carrykim.restapi.accounts.model.Account;
import com.carrykim.restapi.accounts.model.AccountRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    UserRepository userRepository;

    public Account creatAccount(){
        return Account.builder()
                .name("kimseonjin616")
                .password("password")
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
    }

    @Test
    public void findByUserName(){
        Account account = creatAccount();
        userRepository.save(account);

        UserDetails userdetails = accountService.loadUserByUsername(account.getName());

        assertEquals(userdetails.getUsername(), account.getName());
        assertEquals(userdetails.getPassword(), account.getPassword());
    }

}