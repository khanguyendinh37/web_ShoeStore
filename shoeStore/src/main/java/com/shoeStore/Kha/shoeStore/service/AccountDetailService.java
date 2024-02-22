package com.shoeStore.Kha.shoeStore.service;

import com.shoeStore.Kha.shoeStore.Model.account;
import com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountDetailService implements UserDetailsService {
    @Autowired
    accountReporsitory accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<account> byId = accountRepository.findById(username);
        if (byId.isPresent()) {
            account account = byId.get();
            List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
            if (account.getPostion() == 1 && account.getProperti() == 0) {
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
                grantList.add(authority);
            } else if (account.getPostion() == 0 && account.getProperti() == 0) {
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_CUSTOMER");
                grantList.add(authority);
            } else {
                throw new UsernameNotFoundException("Login fail");
            }
            UserDetails userDetails = new User(account.getUserName(), account.getPassword(), grantList);
            return userDetails;
        } else {
            throw new UsernameNotFoundException("Login fail");
        }
    }
}
