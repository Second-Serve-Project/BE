package com.secondserve.util;

import com.secondserve.entity.Customer;
import com.secondserve.jwt.JwtUtil;
import com.secondserve.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerUtil {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomerRepository customerRepository;

    public Customer getCustomer(String accessToken){
        return customerRepository.findByCustomerId(jwtUtil.getId(accessToken));
    }
    public long getCustomerId(String accessToken){
        Customer customer = customerRepository.findByCustomerId(jwtUtil.getId(accessToken));
        return customer.getId();
    }
}
