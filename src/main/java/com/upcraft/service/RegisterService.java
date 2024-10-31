package com.upcraft.service;


import com.upcraft.dto.CustomerDto;
import com.upcraft.entity.Customer;
import com.upcraft.event.UserEvent;
import com.upcraft.repository.CustomerRepository;
import com.upcraft.result.ResultStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@AllArgsConstructor
public class RegisterService {
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final ApplicationEventPublisher eventPublisher;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public boolean fetchExistById(String id) {
        return customerRepository.existsByCustomerId(id);
    }


    @Transactional
    public ResultStatus trySignUp(@Valid CustomerDto.Register registerRequest){
        String customerId = registerRequest.getId();
        String encodedPW = encoder.encode(registerRequest.getPassword());

        if(!fetchExistById(customerId)){
                Customer customer = Customer.builder()
                        .customerId(customerId)
                        .password(encodedPW)
                        .build();
                customerRepository.save(customer);
                UserEvent.Register event = new UserEvent.Register(registerRequest.getId(), registerRequest.getPassword());
                eventPublisher.publishEvent(event);
                System.out.println(event);
                return ResultStatus.SIGNUP_MEMBER;
        }
        return ResultStatus.INTERNAL_SERVER_ERROR;
    }
}
