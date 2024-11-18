package com.secondserve.service;

import com.secondserve.dto.CustomerDto;
import com.secondserve.entity.Customer;
import com.secondserve.jwt.JwtProvider;
import com.secondserve.repository.CustomerRepository;
import com.secondserve.util.DtoConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    private JwtProvider jwtProvider;

    @Override
    public CustomerDto.Page fetchCustomerInfo(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("토큰이 비어있습니다.");
        }
        String parsedToken = jwtProvider.bearerParser(token);
        Customer customer = customerRepository
                .findByCustomerId(jwtProvider.getCustomerId(parsedToken))
                .orElseThrow(() -> new RuntimeException("Customer not found"));


        CustomerDto.Page pageDto = DtoConverter.convertToDto(customer, CustomerDto.Page.class);

        return pageDto;
    }

}
