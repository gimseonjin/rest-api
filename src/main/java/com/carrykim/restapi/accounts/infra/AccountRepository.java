package com.carrykim.restapi.accounts.infra;

import com.carrykim.restapi.accounts.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findByName(String name);
    void deleteAll();
}
