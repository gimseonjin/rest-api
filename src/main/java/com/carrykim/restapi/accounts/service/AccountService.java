package com.carrykim.restapi.accounts.service;

import com.carrykim.restapi.accounts.infra.UserRepository;
import com.carrykim.restapi.accounts.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByName(s)
                .orElseThrow(() -> new UsernameNotFoundException(s));
    }

    public Account create(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return userRepository.save(account);
    }
}
