package com.secondserve.service;

import com.secondserve.dto.CustomerDto;

public interface CustomerService {
    public CustomerDto.Page fetchCustomerInfo(String token);
}
