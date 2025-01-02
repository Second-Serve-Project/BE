package com.secondserve.service;

import com.secondserve.dto.CustomerDto;
import com.secondserve.entity.Customer;
import com.secondserve.jwt.JwtUtil;
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
    private JwtUtil jwtUtil;

    @Override
    public CustomerDto.Page fetchCustomerInfo(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("토큰이 비어있습니다.");
        }
        Customer customer = customerRepository
                .findByCustomerId(jwtUtil.getId(token));


        CustomerDto.Page pageDto = DtoConverter.convertToDto(customer, CustomerDto.Page.class);

        return pageDto;
    }

}
