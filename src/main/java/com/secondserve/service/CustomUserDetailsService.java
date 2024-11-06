package com.secondserve.service;

import com.secondserve.entity.Customer;
import com.secondserve.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 스프링 시큐리티에서 사용하는 UserDetails 객체로 반환
        return new org.springframework.security.core.userdetails.User(
                customer.getPassword(),
                customer.getCustomerId(),
                new ArrayList<>() // 권한 설정이 있다면 여기에 추가
        );
    }
}
