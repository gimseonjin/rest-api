package com.carrykim.restapi.accounts.service;

import com.carrykim.restapi.accounts.infra.UserRepository;
import com.carrykim.restapi.accounts.model.Account;
import com.carrykim.restapi.accounts.model.AccountRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    PasswordEncoder passwordEncoder;


    public Account creatAccount(){
        return Account.builder()
                .name("kimseonjin616")
                .password("password")
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
    }

    @Test
    public void find_by_username_success(){
        Account account = creatAccount();
        accountService.create(account);

        UserDetails userdetails = accountService.loadUserByUsername(account.getName());

        assertEquals(userdetails.getUsername(), account.getName());
        assertEquals(account.getPassword(), userdetails.getPassword());
    }

    @Test
    public void find_by_username_not_found(){
        String wrongUsername = "";

        UsernameNotFoundException exception =
                assertThrows(UsernameNotFoundException.class,
                        () -> accountService.loadUserByUsername(wrongUsername));

        assertEquals(exception.getCause(), new UsernameNotFoundException(wrongUsername).getCause());
        assertEquals(exception.getMessage(), new UsernameNotFoundException(wrongUsername).getMessage());
    }

}