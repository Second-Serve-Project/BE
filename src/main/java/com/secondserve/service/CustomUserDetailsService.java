package com.secondserve.service;

import com.secondserve.entity.Customer;
import com.secondserve.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with customerId: " + customerId));

        // UserDetails 객체 반환
        return new User(
                customer.getCustomerId(),          // 사용자 ID로 사용할 customerId
                customer.getPassword(),            // 암호화된 비밀번호
                new ArrayList<>()                  // 권한 설정이 있다면 여기에 추가
        );
    }
}
