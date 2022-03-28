package com.carrykim.restapi.accounts.infra;

import com.carrykim.restapi.accounts.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends AccountRepository, JpaRepository<Account, Integer> {
}
