package com.carrykim.restapi.config;

import com.carrykim.restapi.accounts.model.Account;
import com.carrykim.restapi.accounts.model.AccountRole;
import com.carrykim.restapi.accounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account admin = Account.builder()
                        .name("admin")
                        .password("password")
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();

                Account user = Account.builder()
                        .name("user")
                        .password("password")
                        .roles(Set.of(AccountRole.USER))
                        .build();

                accountService.create(admin);
                accountService.create(user);
            }
        };
    }
}
