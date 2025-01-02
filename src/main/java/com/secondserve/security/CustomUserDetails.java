package com.secondserve.security;


import com.secondserve.entity.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CustomUserDetails implements UserDetails {

    private final Customer customer;

    public CustomUserDetails(Customer customer) {
        this.customer = customer;
    }


    public String getCustomerId(){
        return customer.getCustomerId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("Customer")); // 고객의 역할(Role) 기반 권한 추가
        return authorities;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getName();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {

        return true;
    }

    public boolean isEnabled() {

        return true;
    }
}
