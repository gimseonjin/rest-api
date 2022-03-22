package com.carrykim.restapi.accounts.infra;

import com.carrykim.restapi.accounts.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends UserRepository, JpaRepository<Account, Integer> {
}
